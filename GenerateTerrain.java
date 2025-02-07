// Source code is decompiled from a .class file using FernFlower decompiler.
import java.awt.Color;
import java.util.Random;

public class GenerateTerrain {
   Random r = new Random();
   static double roughness = 1.5;
   static int mapSize = 50;
   static double Size = 2.0;
   static Color G = new Color(155, 155, 155);

   public GenerateTerrain() {
      double[] var1 = new double[mapSize];
      double[] var2 = new double[var1.length];

      for(int var3 = 0; var3 < var1.length / 2; var3 += 2) {
         int var4;
         for(var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] = var2[var4];
            var2[var4] = this.r.nextDouble() * roughness;
         }

         if (var3 != 0) {
            for(var4 = 0; var4 < var1.length / 2; ++var4) {
               Display.TDPolygons.add(new TDpoly(new double[]{Size * (double)var4, Size * (double)var4, Size + Size * (double)var4}, new double[]{Size * (double)var3, Size + Size * (double)var3, Size + Size * (double)var3}, new double[]{var1[var4], var2[var4], var2[var4 + 1]}, G));
               Display.TDPolygons.add(new TDpoly(new double[]{Size * (double)var4, Size + Size * (double)var4, Size + Size * (double)var4}, new double[]{Size * (double)var3, Size + Size * (double)var3, Size * (double)var3}, new double[]{var1[var4], var2[var4 + 1], var1[var4 + 1]}, G));
            }
         }

         for(var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] = var2[var4];
            var2[var4] = this.r.nextDouble() * roughness;
         }

         if (var3 != 0) {
            for(var4 = 0; var4 < var1.length / 2; ++var4) {
               Display.TDPolygons.add(new TDpoly(new double[]{Size * (double)var4, Size * (double)var4, Size + Size * (double)var4}, new double[]{Size * (double)(var3 + 1), Size + Size * (double)(var3 + 1), Size + Size * (double)(var3 + 1)}, new double[]{var1[var4], var2[var4], var2[var4 + 1]}, G));
               Display.TDPolygons.add(new TDpoly(new double[]{Size * (double)var4, Size + Size * (double)var4, Size + Size * (double)var4}, new double[]{Size * (double)(var3 + 1), Size + Size * (double)(var3 + 1), Size * (double)(var3 + 1)}, new double[]{var1[var4], var2[var4 + 1], var1[var4 + 1]}, G));
            }
         }
      }

   }
}
