package view;

import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import model.SoccerFieldModel;

public class SoccerFieldView extends JFrame {
	
	private static final long serialVersionUID = -3920605975221885626L;
	
	private SoccerPanel soccerFieldPanel;
    private JTextArea output;
    private JScrollPane outScroll;
    private Box box;
    private JLabel label;
	
    public SoccerFieldView(SoccerFieldModel model) {
        this.soccerFieldPanel = new SoccerPanel(model);
        initialise();
    }
    
    public void update() {
        soccerFieldPanel.update();    
    }

    public void initialise() {
        Container container = this.getContentPane();     
        label = new JLabel("", SwingConstants.CENTER);

        output = new JTextArea(6, 18); 
        output.setEditable(false);
        
        outScroll = new JScrollPane();
        outScroll.setViewportView(output);
        outScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        box = new Box(BoxLayout.Y_AXIS);
        box.add(soccerFieldPanel);
        box.add(label);
        box.add(outScroll);
        
        container.add(box, SwingConstants.CENTER);
        setSize(800, 600);
        setVisible(true);
    }
    
    public void out(String out) {
        output.append(out + "\n");
        output.setCaretPosition(output.getDocument().getLength());
    }
}