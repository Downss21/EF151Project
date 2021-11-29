import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CollisionPathing {
	
	private static float m1 = 10;
	private static float m2 = 1;
	private static float r1 = .1f;
	private static float r2 = .1f;
	private static int length = 360;
	private static float speed = 1;
	private static float tickrate = 60;
	
	private static float density1 = m1 / (r1 * r1 * (float)Math.PI);
	private static float density2 = m2 / (r2 * r2 * (float)Math.PI);
	
	private static int x = 0;
	private static int y = 1;
	
	public static void main(String[] args) throws IOException {
		float[][] pos = new float[length][2];
		float speedtick = speed / tickrate;
		FileWriter writer = new FileWriter(new File("script.txt"));
		
		
		for (int t = 0 ; t < length; t++) {
			float time = speedtick * t;
			//x coord
			pos[t][x] = -time;
			//y coord
			pos[t][y] = time * time;
		}
		
//		pos[0] = new float[] {0, 0};
//		pos[1] = new float[] {1, 0};
//		pos[2] = new float[] {1, 1};
//		pos[3] = new float[] {0, 1};
//		pos[4] = new float[] {0, 0};
//		pos[5] = new float[] {10, 10};
		writer.write("Scene.Clear;\n");
		writer.write("Sim.frequency = "+tickrate+";\n");
		
		float[] vo1 = new float[] {(pos[1][x] - pos[0][x]) * tickrate, (pos[1][y] - pos[0][y]) * tickrate};
		for (int t = 1; t < length - 1; t++) {
			float[] po1 = pos[t];
			float[] p1 = pos[t + 1];
			float[] v1 = new float[] {(p1[x] - po1[x]) * tickrate, (p1[y] - po1[y]) * tickrate};
			float[] vo2 = new float[] {speed + 1, 0};
			float[] po2 = moveProjectile(((float)t / tickrate) - (1f / tickrate), po1, vo2);
			writer.write("scene.addCircle({"
					+ "pos := ["+po2[x]+", "+po2[y]+"];"
					+ "vel := ["+vo2[x]+", "+vo2[y]+"];"
					+ "radius := "+r2+";"
					+ "restitution := 1; "
					+ "collideSet := 512;"
					+ "heteroCollide := true;"
					+ "density := "+density2+";"
					+ "onCollide := (e) => {"
						//+ "e.other.pos = ["+p1[x]+", "+p1[y]+"];"
						+ "e.other.vel = ["+v1[x]+", "+v1[y]+"];"
						//+ "e.other.vel = [0, 0];"
						+ "Scene.RemoveEntity(e.this);"
					+ "};"
					+ "});\n");
			System.out.println(po2[x] +", "+po2[y]);
			vo1 = v1;
		}
		writer .write("scene.addCircle({"
				+ "pos := ["+pos[0][x]+", "+pos[0][y]+"];"
				+ "vel := [0, 0];"
				+ "radius := "+r1+";"
				+ "restitution := 1;"
				+ "collideSet := 513;"
				+ "heteroCollide := false;"
				+ "density := "+density1+";"
				+ "color := [1.0, 0, 0, 1.0];"
				+ "});");
		writer.close();
	}
	public static float[] calculateVelocity(float[] initialVelocity1, float[] velocity1) {
		float[] vo2 = new float[2];
		//float[] a = new float[2];
		//float[] b = new float[2];
		for (int i = 0; i < 2; i++) {
			float v1 = initialVelocity1[i] - velocity1[i];
			vo2[i] = (v1 * (1 + m1 / m2)) / 2;
			vo2[i] += velocity1[i];
		}
		return vo2;
	}
	
	public static float[] moveProjectile(float time, float[] initialPosition1, float[] initialVelocity2) {
		float mag = (float) Math.sqrt(initialVelocity2[x] * initialVelocity2[x] + initialVelocity2[y] * initialVelocity2[y]);
		float[] p2 = new float[2];
		for (int i = 0; i < 2; i ++) {
			 float vo2 = initialVelocity2[i];
			 float po1 = initialPosition1[i];
			 float unit = vo2 / mag;
			 float po2 = po1 - unit * (r1 * .9f) - unit * r2;
			 p2[i] = po2 - vo2 * time;
		 }
		return p2;
	}

}
