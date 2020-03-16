package fotbollsspel;

import java.awt.*;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class GameView {
   private int height, width;
   private String title;

   private JFrame jf; // Fönstret
   private Canvas canvas; // ritytan där renderingen sker

   public GameView(int width, int height, String title) {
       this.height = height;
       this.width = width;
       this.title = title;

       // Skapar vår rityta canvas med rätt bredd och höjd
       canvas = new Canvas();
       canvas.setSize(new Dimension(width, height));

       // Skapar fönstret.
       jf = new JFrame(title);
       // Lägger in ritytan i fönstret.
       jf.add(canvas);

       // Lite inställningar
       jf.setResizable(false); // Går ej att ändra storlek på fönster
       jf.pack(); // Packar så att inget tomrum visas
       jf.setLocationRelativeTo(null); // Placeras i mitten på skärmen
       
       // Går att stånga av med x-rutan
       jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
       jf.setIgnoreRepaint(true); // Ritas inte om av JVM.       
       jf.setVisible(true); // Gör allt synligt!
       canvas.requestFocus(); // Ger vår canvas fokus
   }
}

