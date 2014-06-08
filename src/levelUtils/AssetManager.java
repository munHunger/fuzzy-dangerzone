package levelUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AssetManager extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5954920437247964311L;

	public AssetManager(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(400, 400);
		AssetCreator assetCreator = new AssetCreator();
		super.add(assetCreator);
	}
	
	private class AssetCreator extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1672496081579793341L;
		
		private FilePicker assetName = new FilePicker();
		private FilePicker modelName = new FilePicker();
		private FilePicker materialName = new FilePicker();
		private JCheckBox tickBox = new JCheckBox("Walkable");
		private JComboBox<String> onWalkOn = new JComboBox<>(AssetFunctions.getKeyList());
		private JComboBox<String> onWalkOff = new JComboBox<>(AssetFunctions.getKeyList());
		private JComboBox<String> onHit = new JComboBox<>(AssetFunctions.getKeyList());
		public AssetCreator(){
			super.setBackground(Color.PINK);
			super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			super.add(new JLabel("AssetPath"));
			super.add(assetName);
			super.add(new JLabel("ModelPath"));
			super.add(modelName);
			super.add(new JLabel("MaterialPath"));
			super.add(materialName);
			super.add(tickBox);
			super.add(new JLabel("onWalkOn Function"));
			super.add(onWalkOn);
			super.add(new JLabel("onWalkOff Function"));
			super.add(onWalkOff);
			super.add(new JLabel("onHit Function"));
			super.add(onHit);
			
			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(assetName.getPath().equals(""))
						return;
					Asset toSave = new Asset(assetName.getPath());
					if(!modelName.getPath().equals(""))
						toSave.loadModel(modelName.getPath(), materialName.getPath());
					toSave.setWalkable(tickBox.isSelected());
					toSave.onWalkOn = (String) onWalkOn.getSelectedItem();
					toSave.saveAsset();
				}
			});
			super.add(saveButton);
		}
	}
	
	private class FilePicker extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6457173728930652779L;
		
		private JTextField activeFile = new JTextField();
		public FilePicker(){
			activeFile.setEditable(false);
			super.setLayout(new BorderLayout());
			super.add(activeFile, BorderLayout.CENTER);
			JButton fileButton = new JButton("Browse");
			fileButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					File f = new File("res/");
					chooser.setCurrentDirectory(f);
				    int returnVal = chooser.showOpenDialog(new JFrame());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				       activeFile.setText(chooser.getSelectedFile().getAbsolutePath());
				    }

				}
			});
			super.add(fileButton, BorderLayout.EAST);
		}
		public String getPath(){
			return activeFile.getText();
		}
	}
}