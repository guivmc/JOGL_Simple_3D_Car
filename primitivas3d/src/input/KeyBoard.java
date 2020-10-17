package input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import cena.Cena;
/**
 *
 * @author Siabreu
 */
public class KeyBoard implements KeyListener{
    private Cena cena;
    
    public KeyBoard(Cena cena){
        this.cena = cena;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        
        switch (e.getKeyChar()) {            
            case 'r':
                cena.ang += 90.0f;
                cena.sideView = !cena.sideView;
                if(cena.op >= 4)
                    cena.op = 0;
                else
                    cena.op++;
                System.out.println(cena.op);
                break;
        }         
    }

    @Override
    public void keyReleased(KeyEvent e) { }

}
