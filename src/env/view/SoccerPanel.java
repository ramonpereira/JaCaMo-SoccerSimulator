package view;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.SoccerFieldModel;

public class SoccerPanel extends JPanel {

	private static final long serialVersionUID = 1976875439576982160L;
	
    private int cellWidth, cellHeight;
    private SoccerFieldModel soccerFieldModel;
    private Image fieldImage, ballImage, redPlayerImage, bluePlayerImage;

    public SoccerPanel(SoccerFieldModel soccerFieldModel) {
        update();   
        this.soccerFieldModel = soccerFieldModel;
        cellWidth = 16;
        cellHeight = 16;     
        
        setSize(1000, 500);
        
        try {
			this.fieldImage = ImageIO.read(this.getClass().getResource("/images/field.png"));
			this.ballImage = ImageIO.read(this.getClass().getResource("/images/ball.png"));
			this.redPlayerImage = ImageIO.read(this.getClass().getResource("/images/redPlayer.png"));
			this.bluePlayerImage = ImageIO.read(this.getClass().getResource("/images/bluePlayer.png"));
	        Dimension size = new Dimension(800, 600);
	        setPreferredSize(size);
	        setMinimumSize(size);
	        setMaximumSize(size);
	        setSize(size);
	        setLayout(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	/* Field */
    	g.drawImage(fieldImage, 0, 0, null);
          
        /* Agents - Players */
        for(int i=1; i <= SoccerFieldModel.NUM_PLAYERS; i++){
        	Location l = soccerFieldModel.getPlayers().get("player"+i).getLocation();
        	if(i > 10) g.drawImage(bluePlayerImage, l.x*cellWidth +3, l.y*cellHeight +3, null);
        	else g.drawImage(redPlayerImage, l.x*cellWidth +3, l.y*cellHeight +3, null);
        }

        /* Ball */
        Location l = soccerFieldModel.getBallLocation();
        g.drawImage(ballImage, l.x*cellWidth +3, l.y*cellHeight +3, null); 
    }

    public void update() {
        repaint();
    }
    
    public Dimension getPreferredSize() {
    	return new Dimension(800, 400); 
    }
    
    public static void main(String[] args) {
    }
}
