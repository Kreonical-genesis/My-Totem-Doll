package net.lopymine.mtd.api;

import com.google.gson.*;
import org.apache.http.client.HttpResponseException;



import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.skin.data.ParsedSkinData;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.jetbrains.annotations.*;

//? if >=1.20.2 {
import com.mojang.util.UndashedUuid;
//?} else {
/*import com.mojang.util.UUIDTypeAdapter;
 *///?}

public class MojangAPI {

	private static final Gson GSON = new GsonBuilder().setLenient().disableHtmlEscaping().create();

	public static boolean useFallbackAPI = false;

	private static String getUUIDEndpoint(String nickname) {
		if (MojangAPI.useFallbackAPI) {
			return "https://api.mojang.com/users/profiles/minecraft/" + nickname;
		}
		return "https://api.minecraftservices.com/minecraft/profile/lookup/name/" + nickname;
	}

	public static Response<UUID> getUUID(String nickname, boolean canRetry) {
		boolean debugLogEnabled = MyTotemDollClient.getConfig().isDebugLogEnabled();
		int statusCode = -1;
		String responseBody = "Not reached";

		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(getUUIDEndpoint(nickname)))
					.build();
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			statusCode = response.statusCode();
			responseBody = response.body();

			if (statusCode == 429) {
				if (debugLogEnabled) {
					MyTotemDollClient.LOGGER.warn("Received Too Many Requests on {}", nickname);
				}
				return Response.empty(statusCode);
			}
			if (statusCode == 404) {
				if (debugLogEnabled) {
					MyTotemDollClient.LOGGER.warn("Failed to find player profile with nickname {}", nickname);
				}
				return Response.empty(statusCode);
			}

			JsonObject jsonObject = GSON.fromJson(response.body(), JsonObject.class);

			if (statusCode != 200) {
				if (jsonObject.has("errorMessage")) {
					throw new HttpResponseException(statusCode, jsonObject.get("errorMessage").getAsString());
				}
				if (jsonObject.has("error")) {
					throw new HttpResponseException(statusCode, jsonObject.get("error").getAsString());
				}
				throw new HttpResponseException(statusCode, "Unknown error, check status code.");
			}
			if (!jsonObject.has("id")) {
				throw new HttpResponseException(statusCode, "Response doesn't contains 'id'");
			}
			String uuidAsString = jsonObject.get("id").getAsString();
			//? if >=1.20.2 {
			UUID uuid = UndashedUuid.fromStringLenient(uuidAsString);
			//?} else {
			/*UUID uuid = UUIDTypeAdapter.fromString(uuidAsString);
			 *///?}
			return new Response<>(statusCode, uuid);
		} catch (InterruptedException ignored) {
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to get UUID: ", e);
			MyTotemDollClient.LOGGER.error("Response Body: ");
			MyTotemDollClient.LOGGER.error(responseBody);
		}

		if (statusCode == 403) {
			MyTotemDollClient.LOGGER.error("Received 403 status code, using fallback API for UUIDs!");
			MojangAPI.useFallbackAPI = true;
			if (canRetry) {
				return getUUID(nickname, false);
			}
		}

		return Response.empty(statusCode);
	}

	public static Response<ParsedSkinData> getSkinData(String nickname) {
		Response<UUID> response = MojangAPI.getUUID(nickname, true);
		if (response.statusCode() == -1 && response.isEmpty()) { // Other errors
			return Response.empty(response.statusCode());
		}
		if (response.statusCode() == 404 && response.isEmpty()) { // Player not found
			return Response.empty(response.statusCode());
		}
		if (response.statusCode() == 429 && response.isEmpty()) { // Too many requests
			return Response.empty(response.statusCode());
		}
		if (response.isEmpty()) {
			return Response.empty(response.statusCode());
		}
		return MojangAPI.getSkinData(response.value(), nickname);
	}

	@Nullable
	public static Response<ParsedSkinData> getSkinData(UUID uuid, String nickname) {
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid.toString())))
					.build();
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			JsonObject jsonObject = GSON.fromJson(response.body(), JsonObject.class);
			if (jsonObject == null) {
				return Response.empty(response.statusCode());
			}

			ParsedSkinData parsedSkinData = MojangAPI.parseSkinData(jsonObject);
			if (parsedSkinData.getSkinUrl() == null) {
				return Response.empty(response.statusCode());
			}

			return new Response<>(response.statusCode(), parsedSkinData);
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to load skin textures for {}: ", nickname, e);
		}
		return null;
	}

	private static @NotNull ParsedSkinData parseSkinData(@NotNull JsonObject jsonObject) {
		String skinUrl = null;
		String capeUrl = null;
		String skinModel = "default";

		for (JsonElement jsonElement : jsonObject.get("properties").getAsJsonArray()) {
			JsonObject propertiesObject = jsonElement.getAsJsonObject();
			if (!propertiesObject.get("name").getAsString().equals("textures")) {
				continue;
			}
			JsonElement valueElement = propertiesObject.get("value");
			String jsonSkinData = new String(Base64.getDecoder().decode(valueElement.getAsString()), StandardCharsets.UTF_8);
			JsonObject jsonSkinObject = GSON.fromJson(jsonSkinData, JsonObject.class);
			JsonObject skinTexturesObject = jsonSkinObject.get("textures").getAsJsonObject();

			JsonObject skinObject = skinTexturesObject.get("SKIN").getAsJsonObject();
			skinUrl = skinObject.get("url").getAsString();

			if (skinTexturesObject.has("CAPE")) {
				JsonObject capeObject = skinTexturesObject.get("CAPE").getAsJsonObject();
				capeUrl = capeObject.get("url").getAsString();
			}

			if (skinObject.has("metadata")) {
				JsonObject skinMetadataObject = skinObject.get("metadata").getAsJsonObject();
				skinModel = skinMetadataObject.get("model").getAsString();
			}
			break;
		}

		return new ParsedSkinData(skinUrl, capeUrl, null, skinModel.equals("slim"));
	}
}
