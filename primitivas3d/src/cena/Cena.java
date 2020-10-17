package cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Siabreu
 */
public class Cena implements GLEventListener {
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private TextRenderer textRenderer;

    public float ang;
    public int op;

    public boolean sideView;

    //dados do cubo
    public float size;

    //dados da esfera
    public float radio;
    public int slices;
    public int stacks;

    //dados do cone
    public float height;

    //dados do torus
    public float innerRadius;
    public float outerRadius;
    public int rings;

    //Preenchimento
    public int mode;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena        
        GL2 gl = drawable.getGL().getGL2();
        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -100;
        xMax = yMax = zMax = 100;

        reset();

        textRenderer = new TextRenderer(new Font("Comic Sans MS Negrito", Font.BOLD, 15));
        //Habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    public void reset() {
        ang = 0;

        sideView = true;

        //dados do cubo
        size = 50;

        //dados da esfera
        radio = 50;
        slices = 15;
        stacks = 15;

        //dados do cone
        height = 50;

        //dados do torus
        innerRadius = 10;
        outerRadius = 50;
        rings = 6;

        //preenchimento
        mode = GL2.GL_FILL;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        //objeto para desenho 3D
        GLUT glut = new GLUT();

        GLU glu = new GLU();

        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 0);
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); //ler a matriz identidade
        
        /*
            desenho da cena        
        *
        */

        //Modo de desenho - os parametros s�o constantes inteiras
        //int modo =  GL2.GL_FILL; ou GL2.GL_LINE ou GL2.GL_POINT        
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);

        drawCubes(gl, glut);

        drawWheels(gl, glut, sideView ? 45 : 20);

        if(!sideView)
        {
            drawLights(gl, glut, op == 0 || op == 1 ? 0.8f : 0);
        }


        gl.glFlush();
    }

    public void dadosObjeto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, mode);
    }

    private void drawCubes(GL2 gl, GLUT glut)
    {
        //Cube
        gl.glColor3f(0, 0.2f, 0.8f); //cor do objeto
        gl.glPushMatrix();
        gl.glRotated(ang, 0, 1, 0);
        gl.glTranslated(23, 20, 0);
        gl.glScalef(1.5f, 1, 1);
        glut.glutSolidCube(30);
        gl.glPopMatrix();

        //Rect
        gl.glColor3f(0, 0, 0.8f); //cor do objeto
        gl.glPushMatrix();
        gl.glRotated(ang, 0, 1, 0);
        gl.glScalef(3, 1, 1);
        glut.glutSolidCube(30);
        gl.glPopMatrix();
    }

    private void drawWheels(GL2 gl, GLUT glut, int xTranslation)
    {
        gl.glColor3f(0.4f, 0.2f, 0.8f); //cor do objeto
        gl.glPushMatrix();
        gl.glTranslated(xTranslation, -15, 100);
        gl.glRotated(ang, 0, 1, 0);
        glut.glutSolidTorus(5, 10, 15, 360);
        gl.glPopMatrix();

        gl.glColor3f(0.4f, 0.2f, 0.8f); //cor do objeto
        gl.glPushMatrix();
        gl.glTranslated(-xTranslation, -15, 100);
        gl.glRotated(ang, 0, 1, 0);
        glut.glutSolidTorus(5, 10, 15, 360);
        gl.glPopMatrix();
    }

    private void drawLights(GL2 gl, GLUT glut, float green)
    {
        gl.glColor3f(0.8f, green, 0); //cor do objeto
        gl.glPushMatrix();
        gl.glTranslated(10, 1, 100);
        gl.glRotated(ang, 0, 1, 1);
        glut.glutSolidSphere(2, slices, stacks);
        gl.glPopMatrix();

        gl.glColor3f(0.8f, green, 0); //cor do objeto
        gl.glPushMatrix();
        gl.glTranslated(-10, 1, 100);
        gl.glRotated(ang, 0, 1, 1);
        glut.glutSolidSphere(2, slices, stacks);
        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();

        //evita a divisao por zero
        //if(height == 0) height = 1;
        //calcula a proporcao da janela (aspect ratio) da nova janela
        //float aspect = (float) width / height;

        //seta o viewport para abranger a janela inteira
        //gl.glViewport(0, 0, width, height);

        //ativa a matriz de projecao
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //ler a matriz identidade

        //Projecao ortogonal
        //true:   aspect >= 1 configura a altura de -1 para 1 : com largura maior
        //false:  aspect < 1 configura a largura de -1 para 1 : com altura maior
//        if(width >= height)            
//            gl.glOrtho(xMin * aspect, xMax * aspect, yMin, yMax, zMin, zMax);
//        else        
//            gl.glOrtho(xMin, xMax, yMin / aspect, yMax / aspect, zMin, zMax);

        //projecao ortogonal sem a correcao do aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);

        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //ler a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}
