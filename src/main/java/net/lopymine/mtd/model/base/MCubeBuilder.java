package net.lopymine.mtd.model.base;

import lombok.Getter;
import net.minecraft.client.model.*;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

import java.util.*;

@SuppressWarnings("unused")
@Getter
public class MCubeBuilder {

	private final float x;
	private final float y;
	private final float z;
	private final float xSize;
	private final float ySize;
	private final float zSize;
	private final Map<Direction, MQuadBuilder> sideBuilders = new HashMap<>();
	private Dilation dilation = Dilation.NONE;

	private MCubeBuilder(float x, float y, float z, float xSize, float ySize, float zSize) {
		this.x     = x;
		this.y     = y;
		this.z     = z;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
	}

	public static MCubeBuilder blockBenchBuilder(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
		float xSize = toX - fromX;
		float ySize = toY - fromY;
		float zSize = toZ - fromZ;
		float x = 0 - toX;
		float y = -fromY - ySize + 0;
		float z = fromZ - 0;
		return new MCubeBuilder(x, y, z, xSize, ySize, zSize);
	}

	public static MCubeBuilder builder(float x, float y, float z, float xSize, float ySize, float zSize) {
		return blockBenchBuilder(x, y, z, x + xSize, y + ySize, z + zSize);
	}

	public MCubeBuilder withSide(float fromU, float fromV, float toU, float toV, Direction direction) {
		return this.withSide(fromU, fromV, toU, toV, direction, 0);
	}

	public MCubeBuilder withSide(float fromU, float fromV, float toU, float toV, Direction direction, int rotation) {
		this.sideBuilders.put(direction, new MQuadBuilder(fromU, fromV, toU, toV, direction, rotation));
		return this;
	}

	public MCubeBuilder withDefaultSides(float startU, float startV) {
		float j = startU;
		float k = startU + this.zSize;
		float l = startU + this.zSize + this.xSize;
		float m = startU + this.zSize + this.xSize + this.xSize;
		float n = startU + this.zSize + this.xSize + this.zSize;
		float o = startU + this.zSize + this.xSize + this.zSize + this.xSize;
		float p = startV;
		float q = startV + this.zSize;
		float r = startV + this.zSize + this.ySize;

		this.withSide(l, q, m, p, Direction.UP);
		this.withSide(k, p, l, q, Direction.DOWN);
		this.withSide(l, q, n, r, Direction.EAST);
		this.withSide(j, q, k, r, Direction.WEST);
		this.withSide(k, q, l, r, Direction.NORTH);
		return this.withSide(n, q, o, r, Direction.SOUTH);
	}

	public MCubeBuilder withDilation(float radius) {
		return this.withDilation(new Dilation(radius));
	}

	public MCubeBuilder withDilation(Dilation dilation) {
		this.dilation = dilation;
		return this;
	}

	public MCubeBuilder withoutSide(Direction direction) {
		this.sideBuilders.remove(direction);
		return this;
	}

	public MCuboid build(int textureWidth, int textureHeight, ModelTransform rootTransform) {
		Vector3f pos = new Vector3f(this.x - rootTransform.pivotX, this.y - rootTransform.pivotY, this.z - rootTransform.pivotZ);
		Vector3f size = new Vector3f(this.xSize, this.ySize, this.zSize);
		Dilation dilation = this.dilation;

		Quad[] quads = this.sideBuilders.values().stream().map(sideBuilder -> sideBuilder.build(textureWidth, textureHeight, pos, size, dilation)).toList().toArray(new Quad[0]);

		return new MCuboid(pos, size, quads, dilation);
	}
}
