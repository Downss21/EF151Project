import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CollisionPathing {
	
	private static float m1 = 10;
	private static float m2
	private static float[][] pos;
	private static float r1 = .1f;
	private static float r2 = .1f;
	private static int length = 600;
	private static float speed = 10;
	private static int tickspeed = 60;
	
	private static float density = m / (r2 * r2 * (float)Math.PI);
	
	private static int x = 0;
	private static int y = 1;
	
	public static void main(String[] args) throws IOException {
		pos = new float[length][2];
		float speedtick = speed / tickspeed;
		FileWriter writer = new FileWriter(new File("script.txt"));
		
		for (int t = 0 ; t < length; t++) {
			float time = speedtick * t;
			//x coord
			pos[t][x] = time;
			//y coord
			pos[t][y] = time * time;
		}
		
		float[] vo1 = new float[2];
		for (int t = 0; t < length - 1; t++) {
			float[] po1 = pos[t];
			float[] p1 = pos[t + 1];
			float[] v1 = new float[] {p1[x] - po1[x], p1[y] - po1[y]};
			float[] vo2 = calculateVelocity(vo1, v1);
			float[] po2 = moveProjectile(10f, po1, vo2);
			writer.write("scene.addCircle({pos := ["+po2[x]+", "+po2[y]+"]; vel := ["+vo2[x]+", "+vo2[y]+"]; radius := "+r2+"; restitution := 1; collideSet := 512; heteroCollide := true; density := "+density+";});\n");
			System.out.println(po2[x] +", "+po2[y]);
		}
		writer.close();
	}
	public static float[] calculateVelocity(float[] initialVelocity1, float[] velocity1) {
		float[] vo2 = new float[2];
		//float[] a = new float[2];
		//float[] b = new float[2];
		for (int i = 0; i < 2; i++) {
			float vo1 = initialVelocity1[i];
			float v1 = velocity1[i];
			float b = (v1 * m) - (vo1 * m);
			float a = (.5f * m * v1 * v1) + ((vo1 * vo1 * m) / (2f * m)) - ((vo1 * v1 * m * m) / m) + ((v1 * v1 * m * m) / (2 * m)) - (.5f * m * vo1 * vo1);
			vo2[i] = a / b;
		}
		return vo2;
	}
	
	public static float[] moveProjectile(float distance, float[] initialPosition1, float[] initialVelocity2) {
		float mag = (float) Math.sqrt(initialVelocity2[x] * initialVelocity2[x] + initialVelocity2[y] * initialVelocity2[y]);
		float[] p2 = new float[2];
		for (int i = 0; i < 2; i ++) {
			 float vo2 = initialVelocity2[i];
			 float po1 = initialPosition1[i];
			 float unit = vo2 / mag;
			 float po2 = po1 - unit * r1 - unit * r2;
			 p2[i] = po2 - vo2 * time;
		 }
		return p2;
	}

}
