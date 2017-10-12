package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.DbLogin;

/**
 * The LoginDialog is a swing JDialog for admin login.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
@SuppressWarnings("serial")
public class LoginDialog extends JDialog {

	private JLabel nameLabel = new JLabel("Username: ");
	private JLabel passwordLabel = new JLabel("Password: ");
	private JTextField nameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JButton okButton = new JButton("Login");

	/**
	 * LoginDialog constructor.
	 */
	public LoginDialog() {
		setEnabled(true);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(500, 200);
		setLocationRelativeTo(null);

		this.setTitle("Aggregator Manager Login");

		JPanel topPanel = new JPanel(new GridBagLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		buttonPanel.add(okButton);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(4, 4, 4, 4);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		topPanel.add(nameLabel, gbc);

		GridBagConstraints gbd = new GridBagConstraints();
		gbd.fill = GridBagConstraints.HORIZONTAL;
		gbd.insets = new Insets(4, 4, 4, 4);

		gbd.gridx = 1;
		gbd.gridy = 0;
		gbd.weightx = 1;
		topPanel.add(nameField, gbd);

		GridBagConstraints gbe = new GridBagConstraints();
		gbe.insets = new Insets(4, 4, 4, 4);

		gbe.gridx = 0;
		gbe.gridy = 1;
		gbe.fill = GridBagConstraints.NONE;
		gbe.weightx = 0;
		topPanel.add(passwordLabel, gbe);

		GridBagConstraints gbf = new GridBagConstraints();
		gbf.insets = new Insets(4, 4, 4, 4);

		gbf.fill = GridBagConstraints.HORIZONTAL;
		gbf.gridx = 1;
		gbf.gridy = 1;
		gbf.weightx = 1;
		topPanel.add(passwordField, gbf);

		getContentPane().add(topPanel);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});

		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		LoginDialog.this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("You need to login to continue.");
				e.getWindow().dispose();
				System.exit(1);
			}
		});
		setVisible(true);
	}

	/**
	 * Login function. Calls database function to check if given credentials are
	 * correct.
	 */
	private void login() {
		try {
			if (DbLogin.login(nameField.getText(), String.valueOf(passwordField.getPassword())) == true) {
				System.out.println("Welcome " + nameField.getText() + "!");
				dispose();
			} else {
				System.out.println("Could not login. Now exiting.");
				dispose();
				System.exit(1);
			}
		} catch (SQLException e) {
			System.err.println("Please check your database connectivity: " + e.getMessage());
			dispose();
			System.exit(-1);
		}
	}
}