package levelUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AssetManager extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5954920437247964311L;

	private AssetBrowser assetBrowser = new AssetBrowser();
	private static final Color bgColor = new Color(67,78,82);
	private static final Color fgColor = new Color(110,124,128);
	private static final Color buttonColor = new Color(52,63,66);
	
	public AssetManager(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(400, 400);
		AssetCreator assetCreator = new AssetCreator();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(assetBrowser);
		mainPanel.add(assetCreator);
		super.add(mainPanel);
		super.pack();
	}
	
	public File getSelectedAsset(){
		return assetBrowser.getSelectedAsset();
	}
	
	private class AssetBrowser extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3580948964358563834L;
		private ArrayList<File> assets = new ArrayList<>();
		private JList<String> assetList;
		public AssetBrowser(){
			super.setBackground(bgColor);
			super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			assetList = new JList<String>(toRelativePathArray());
			assetList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			assetList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			assetList.setVisibleRowCount(-1);
			JScrollPane listScroller = new JScrollPane(assetList);
			listScroller.setPreferredSize(new Dimension(40, 80));
			JLabel assetBrowserLabel = new JLabel("AssetBrowser");
			assetBrowserLabel.setForeground(fgColor);
			super.add(assetBrowserLabel);
			assetList.setBackground(bgColor);
			assetList.setForeground(fgColor);
			super.add(listScroller);
			
			JButton deleteButton = new JButton("Delete Selected");
			deleteButton.setBackground(buttonColor);
			deleteButton.setForeground(fgColor);
			deleteButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(assetList.getSelectedIndex() != -1){
						assets.remove(assetList.getSelectedIndex());
						assetList.setListData(toRelativePathArray());
					}
				}
			});
			super.add(deleteButton);
			
			JButton saveButton = new JButton("Save Asset Set");
			saveButton.setBackground(buttonColor);
			saveButton.setForeground(fgColor);
			saveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser chooser = new JFileChooser();
					File f = new File("res/");
					chooser.setCurrentDirectory(f);
				    int returnVal = chooser.showSaveDialog(new JFrame());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				       try {
						ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()));
						outStream.writeObject(assets);
						outStream.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    }
				}
			});
			
			JButton loadButton = new JButton("Load Asset Set");
			loadButton.setBackground(buttonColor);
			loadButton.setForeground(fgColor);
			loadButton.addActionListener(new ActionListener(){
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e){
					JFileChooser chooser = new JFileChooser();
					File f = new File("res/");
					chooser.setCurrentDirectory(f);
				    int returnVal = chooser.showOpenDialog(new JFrame());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				       try {
						ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
						assets = (ArrayList<File>) inStream.readObject();
						assetList.setListData(toRelativePathArray());
						inStream.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    }
				}
			});
			super.add(saveButton);
			super.add(loadButton);
		}
		
		public void addAsset(File f){
			assets.add(f);
			assetList.setListData(toRelativePathArray());
		}
		
		private String[] toRelativePathArray(){
			String[] toReturn = new String[assets.size()];
			int i = 0;
			for(File f : assets){
				toReturn[i] = f.getName();
				i++;
			}
			return toReturn;
		}
		
		private File getSelectedAsset(){
			return assets.get(assetList.getSelectedIndex());
		}
	}
	
	private class AssetCreator extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1672496081579793341L;
		
		private FilePicker assetName = new FilePicker("asset");
		private FilePicker modelName = new FilePicker("obj");
		private FilePicker materialName = new FilePicker("mtl");
		private JCheckBox tickBox = new JCheckBox("Walkable");
		private JComboBox<String> onWalkOn = new JComboBox<>(AssetFunctions.getKeyList());
		private JComboBox<String> onWalkOff = new JComboBox<>(AssetFunctions.getKeyList());
		private JComboBox<String> onHit = new JComboBox<>(AssetFunctions.getKeyList());
		public AssetCreator(){
			super.setBackground(bgColor);
			super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			onWalkOn.setBackground(bgColor);
			onWalkOff.setBackground(bgColor);
			onHit.setBackground(bgColor);
			
			JLabel assetPathLabel = new JLabel("AssetPath");
			assetPathLabel.setForeground(fgColor);
			super.add(assetPathLabel);
			super.add(assetName);
			JLabel modelPathLabel = new JLabel("ModelPath");
			modelPathLabel.setForeground(fgColor);
			super.add(modelPathLabel);
			super.add(modelName);
			JLabel materialPathLabel = new JLabel("MaterialPath");
			materialPathLabel.setForeground(fgColor);
			super.add(materialPathLabel);
			super.add(materialName);
			tickBox.setForeground(fgColor);
			tickBox.setBackground(bgColor);
			super.add(tickBox);
			JLabel onWalkOnLabel = new JLabel("onWalkOn Function");
			onWalkOnLabel.setForeground(fgColor);
			super.add(onWalkOnLabel);
			super.add(onWalkOn);
			JLabel onWalkOffLabel = new JLabel("onWalkOff Function");
			onWalkOffLabel.setForeground(fgColor);
			super.add(onWalkOffLabel);
			super.add(onWalkOff);
			JLabel onHitLabel = new JLabel("onHit Function");
			onHitLabel.setForeground(fgColor);
			super.add(onHitLabel);
			super.add(onHit);
			
			JButton saveButton = new JButton("Save");
			saveButton.setBackground(buttonColor);
			saveButton.setForeground(fgColor);
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
					assetBrowser.addAsset(new File(assetName.getPath()));
				}
			});
			super.add(saveButton);
		}
		
		private class FilePicker extends JPanel{
			/**
			 * 
			 */
			private static final long serialVersionUID = -6457173728930652779L;
			
			private JTextField activeFile = new JTextField();
			public FilePicker(final String fileExtension){
				activeFile.setEditable(false);
				activeFile.setBackground(bgColor);
				activeFile.setForeground(fgColor);
				super.setLayout(new BorderLayout());
				super.add(activeFile, BorderLayout.CENTER);
				JButton fileButton = new JButton("Browse");
				fileButton.setBackground(buttonColor);
				fileButton.setForeground(fgColor);
				fileButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						JFileChooser chooser = new JFileChooser();
						FileNameExtensionFilter filter = new FileNameExtensionFilter(fileExtension, fileExtension);
						chooser.setFileFilter(filter);
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
}