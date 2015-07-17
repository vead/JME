package no.jschief.lupercal.poc.waypointing.hud;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import no.jschief.lupercal.poc.waypointing.abilities.Ability;

public class Hud {
	
	private BitmapText selectedInfoTextL1_res;
	private BitmapText selectedInfoTextL2_res;
	private BitmapText selectedInfoTextL3_res;
	private BitmapText selectedInfoTextL4_res;
	
	private BitmapText debugTextL1;
	private BitmapText debugTextL2;
	private BitmapText debugTextL3;
	private BitmapText debugTextL4;
	private BitmapText debugTextL5;
	private BitmapText debugTextL6;
	
	private BitmapText debugTextL1_res;
	private BitmapText debugTextL2_res;
	private BitmapText debugTextL3_res;
	private BitmapText debugTextL4_res;
	private BitmapText debugTextL5_res;
	private BitmapText debugTextL6_res;
	
	private BitmapFont guiFont;
	private Node guiNode;
	private int screenWidth;
	private float lineHeight;
	private int yOffset = 30;
	
	public Hud(BitmapFont guiFont, Node guiNode, int screenWidth) {
		this.guiFont = guiFont;
		this.guiNode = guiNode;
		this.screenWidth = screenWidth;
		this.lineHeight = guiFont.getCharSet().getRenderedSize();
		this.createSelectInfo();
		this.createDebugInfo();
	}
	
	private void createSelectInfo() {
		ColorRGBA color = new ColorRGBA(0.4f,1.0f,0.4f,1f);


		BitmapText selectedInfoTextL1 = new BitmapText(guiFont, false);
		BitmapText selectedInfoTextL2 = new BitmapText(guiFont, false);
		BitmapText selectedInfoTextL3 = new BitmapText(guiFont, false);
		BitmapText selectedInfoTextL4 = new BitmapText(guiFont, false);

		selectedInfoTextL1_res = new BitmapText(guiFont, false);
		selectedInfoTextL2_res = new BitmapText(guiFont, false);
		selectedInfoTextL3_res = new BitmapText(guiFont, false);
		selectedInfoTextL4_res = new BitmapText(guiFont, false);

		selectedInfoTextL1.setSize(lineHeight);
		selectedInfoTextL2.setSize(lineHeight);
		selectedInfoTextL3.setSize(lineHeight);
		selectedInfoTextL4.setSize(lineHeight);
		
		selectedInfoTextL1.setColor(color);   
		selectedInfoTextL1.setColor(color);   
		selectedInfoTextL1.setColor(color);   
		selectedInfoTextL1.setColor(color);
		
		selectedInfoTextL1_res.setColor(color);   
		selectedInfoTextL2_res.setColor(color);   
		selectedInfoTextL3_res.setColor(color);   
		selectedInfoTextL4_res.setColor(color);
		
		selectedInfoTextL1.setText("SelectedEntity:");
		selectedInfoTextL2.setText("  info1: ");
		selectedInfoTextL3.setText("  info2: ");
		selectedInfoTextL4.setText("  info3: ");
		
		selectedInfoTextL1.setLocalTranslation( 4, lineHeight * 5, 0); 
		selectedInfoTextL2.setLocalTranslation( 4, lineHeight * 4, 0);
		selectedInfoTextL3.setLocalTranslation( 4, lineHeight * 3, 0);
		selectedInfoTextL4.setLocalTranslation( 4, lineHeight * 2, 0);
		
		selectedInfoTextL1_res.setLocalTranslation( selectedInfoTextL1.getLineWidth() + 10, lineHeight * 5, 0);
		selectedInfoTextL2_res.setLocalTranslation( selectedInfoTextL2.getLineWidth() + 10, lineHeight * 4, 0);
		selectedInfoTextL3_res.setLocalTranslation( selectedInfoTextL3.getLineWidth() + 10, lineHeight * 3, 0);
		selectedInfoTextL4_res.setLocalTranslation( selectedInfoTextL4.getLineWidth() + 10, lineHeight * 2, 0);

		//System.out.println("linVelVT.cen: " + linVelBT.toString() + " guiFont...R.size: " + guiFont.getCharSet().getRenderedSize() );

		guiNode.attachChild(selectedInfoTextL1);
		guiNode.attachChild(selectedInfoTextL2);
		guiNode.attachChild(selectedInfoTextL3);
		guiNode.attachChild(selectedInfoTextL4);
		guiNode.attachChild(selectedInfoTextL1_res);
		guiNode.attachChild(selectedInfoTextL2_res);
		guiNode.attachChild(selectedInfoTextL3_res);
		guiNode.attachChild(selectedInfoTextL4_res);
//
//		Picture pic = new Picture("HUD Picture");
//		pic.setImage(assetManager, "Textures/ColoredTex/Monkey.png", true);
//		pic.setWidth(settings.getWidth()/2);
//		pic.setHeight(settings.getHeight()/2);
//		pic.setPosition(settings.getWidth()/4, settings.getHeight()/4);
		//guiNode.attachChild(pic);
		
	}

	private void createDebugInfo() {
		ColorRGBA color = new ColorRGBA(0.4f,1.0f,0.4f,1f);

		debugTextL1 = new BitmapText(guiFont, false);
		debugTextL2 = new BitmapText(guiFont, false);
		debugTextL3 = new BitmapText(guiFont, false);
		debugTextL4 = new BitmapText(guiFont, false);
		debugTextL5 = new BitmapText(guiFont, false);
		debugTextL6 = new BitmapText(guiFont, false);

		debugTextL1_res = new BitmapText(guiFont, false);
		debugTextL2_res = new BitmapText(guiFont, false);
		debugTextL3_res = new BitmapText(guiFont, false);
		debugTextL4_res = new BitmapText(guiFont, false);
		debugTextL5_res = new BitmapText(guiFont, false);
		debugTextL6_res = new BitmapText(guiFont, false);

		debugTextL1.setSize(lineHeight);
		debugTextL2.setSize(lineHeight);
		debugTextL3.setSize(lineHeight);
		debugTextL4.setSize(lineHeight);
		debugTextL5.setSize(lineHeight);
		debugTextL6.setSize(lineHeight);
		
		debugTextL1.setColor(color);   
		debugTextL2.setColor(color);   
		debugTextL3.setColor(color);   
		debugTextL4.setColor(color); 
		debugTextL5.setColor(color); 
		debugTextL6.setColor(color);
		
		debugTextL1_res.setColor(color);   
		debugTextL2_res.setColor(color);   
		debugTextL3_res.setColor(color);   
		debugTextL4_res.setColor(color);
		debugTextL5_res.setColor(color);
		debugTextL6_res.setColor(color);
		
		debugTextL1.setText("  --- ");
		debugTextL2.setText("  --- ");
		debugTextL3.setText("  --- ");
		debugTextL4.setText("  --- ");
		debugTextL5.setText("  --- ");
		debugTextL6.setText("  --- ");
		
		debugTextL1.setLocalTranslation( 4, lineHeight * 12, 0); 
		debugTextL2.setLocalTranslation( 4, lineHeight * 11, 0);
		debugTextL3.setLocalTranslation( 4, lineHeight * 10, 0);
		debugTextL4.setLocalTranslation( 4, lineHeight * 9, 0);
		debugTextL5.setLocalTranslation( 4, lineHeight * 8, 0);
		debugTextL6.setLocalTranslation( 4, lineHeight * 7, 0);
		
		debugTextL1_res.setLocalTranslation( debugTextL1.getLineWidth() + 20, lineHeight * 12, 0);
		debugTextL2_res.setLocalTranslation( debugTextL2.getLineWidth() + 20, lineHeight * 11, 0);
		debugTextL3_res.setLocalTranslation( debugTextL3.getLineWidth() + 20, lineHeight * 10, 0);
		debugTextL4_res.setLocalTranslation( debugTextL4.getLineWidth() + 20, lineHeight * 9, 0);
		debugTextL5_res.setLocalTranslation( debugTextL5.getLineWidth() + 20, lineHeight * 8, 0);
		debugTextL6_res.setLocalTranslation( debugTextL6.getLineWidth() + 20, lineHeight * 7, 0);

		//System.out.println("linVelVT.cen: " + linVelBT.toString() + " guiFont...R.size: " + guiFont.getCharSet().getRenderedSize() );

		guiNode.attachChild(debugTextL1);
		guiNode.attachChild(debugTextL2);
		guiNode.attachChild(debugTextL3);
		guiNode.attachChild(debugTextL4);
		guiNode.attachChild(debugTextL5);
		guiNode.attachChild(debugTextL6);
		guiNode.attachChild(debugTextL1_res);
		guiNode.attachChild(debugTextL2_res);
		guiNode.attachChild(debugTextL3_res);
		guiNode.attachChild(debugTextL4_res);
		guiNode.attachChild(debugTextL5_res);
		guiNode.attachChild(debugTextL6_res);
	}
	
	public void addAbility( Ability ability ) {
		int xOffset = screenWidth - 130;
		BitmapText abilityBMText = new BitmapText(guiFont, false);
		abilityBMText.setColor(ColorRGBA.Gray);
		abilityBMText.setSize(lineHeight);
		abilityBMText.setText(ability.getName());
		abilityBMText.setName(ability.getName());
		abilityBMText.setLocalTranslation(xOffset, yOffset, 0);
		guiNode.attachChild( abilityBMText );
		yOffset += lineHeight;
	}

	public void setColorByName( ColorRGBA color, String name) {
		((BitmapText)guiNode.getChild(name)).setColor(color);
	}
	
	public BitmapText getSelectedInfoTextL1_res() {
		return selectedInfoTextL1_res;
	}

	public void setSelectedInfoTextL1_res(String selectedInfoTextL1_res) {
		this.selectedInfoTextL1_res.setText( selectedInfoTextL1_res );
	}

	public BitmapText getSelectedInfoTextL2_res() {
		return selectedInfoTextL2_res;
	}

	public void setSelectedInfoTextL2_res(String selectedInfoTextL2_res) {
		this.selectedInfoTextL2_res.setText( selectedInfoTextL2_res );
	}

	public BitmapText getSelectedInfoTextL3_res() {
		return selectedInfoTextL3_res;
	}

	public void setSelectedInfoTextL3_res(String selectedInfoTextL3_res) {
		this.selectedInfoTextL3_res.setText( selectedInfoTextL3_res );
	}

	public BitmapText getSelectedInfoTextL4_res() {
		return selectedInfoTextL4_res;
	}

	public void setSelectedInfoTextL4_res(String selectedInfoTextL4_res) {
		this.selectedInfoTextL4_res.setText( selectedInfoTextL4_res );
	}
	
	public void setDebug1(String name, String value) {
		this.debugTextL1.setText( name );
		this.debugTextL1_res.setLocalTranslation(debugTextL1.getLineWidth() + 20, debugTextL1_res.getLocalTranslation().y, 0);
		this.debugTextL1_res.setText( value );
	}
	public void setDebug2(String name, String value) {
		this.debugTextL2.setText( name );
		this.debugTextL2_res.setLocalTranslation(debugTextL2.getLineWidth() + 20, debugTextL2_res.getLocalTranslation().y, 0);
		this.debugTextL2_res.setText( value );
	}
	public void setDebug3(String name, String value) {
		this.debugTextL3.setText( name );
		this.debugTextL3_res.setLocalTranslation(debugTextL3.getLineWidth() + 20, debugTextL3_res.getLocalTranslation().y, 0);
		this.debugTextL3_res.setText( value );
	}
	public void setDebug4(String name, String value) {
		this.debugTextL4.setText( name );
		this.debugTextL4_res.setLocalTranslation(debugTextL4.getLineWidth() + 20, debugTextL4_res.getLocalTranslation().y, 0);
		this.debugTextL4_res.setText( value );
	}
	public void setDebug5(String name, String value) {
		this.debugTextL5.setText( name );
		this.debugTextL5_res.setLocalTranslation(debugTextL5.getLineWidth() + 20, debugTextL5_res.getLocalTranslation().y, 0);
		this.debugTextL5_res.setText( value );
	}
	public void setDebug6(String name, String value) {
		this.debugTextL6.setText( name );
		this.debugTextL6_res.setLocalTranslation(debugTextL6.getLineWidth() + 20, debugTextL6_res.getLocalTranslation().y, 0);
		this.debugTextL6_res.setText( value );
	}
}
