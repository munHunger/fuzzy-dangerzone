package levelUtils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

public class AssetManager extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5954920437247964311L;

	public AssetManager(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(100, 300);
		JList<String> assetList = new JList<>();
		assetList.add("test", new JButton("Hello"));
		super.add(assetList);
	}
}
