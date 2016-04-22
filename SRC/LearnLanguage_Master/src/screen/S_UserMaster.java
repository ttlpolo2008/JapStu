package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import component.JTextFieldLimit;
import dto.ComboItem;
import dto.UserDto;
import enums.EditMode;
import enums.UserType;
import services.UserService;
import util.ComponentUtil;
import util.Constants;
import util.MessageUtil;
import util.StringUtil;

public class S_UserMaster {

	// User's Frame
	private JFrame frmUserMaster;

	// Parent's Frame
	private JFrame frmParent;

	// Panel SearchArea
	private JPanel searchPanel;
	// Panel RegisterArea
	private JPanel registerPanel;

	// Combobox UserType
	@SuppressWarnings("rawtypes")
	private JComboBox cbxUserType;

	// Table info
	private JTable tblInfo;

	// TextField UserName
	private JTextField txtUserName;
	// TextField Password
	private JTextField txtPassword;
	// TextField NickName
	private JTextField txtNickName;

	// Button Back
	private JButton btnBack;

	// Data List
	private List<UserDto> userDtoList;
	// Deleted Data List
	private List<Long> deletedList;
	// Editing data
	private UserDto editingUserDto;

	// Edit mode
	private EditMode editMode;

	/**
	 * Create User screen.
	 *
	 * @param frmParent
	 */
	public S_UserMaster(JFrame frmParent) {
		this.frmParent = frmParent;

		// Initialize Components
		initComponents();

		// Initialize combobox
		initComboBox();

		// Initialize screen
		initScreen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frmUserMaster = new JFrame();
		frmUserMaster.setResizable(false);
		frmUserMaster.setTitle("User Master");
		frmUserMaster.setBounds(100, 100, 600, 610);
		frmUserMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUserMaster.getContentPane().setLayout(new BoxLayout(frmUserMaster.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmUserMaster.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		// Create SearchPanel
		searchPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, searchPanel, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, searchPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, searchPanel, 400, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, searchPanel, 0, SpringLayout.EAST, panel);
		panel.add(searchPanel);

		// Initialize SearchArea.
		initSearchArea();

		// Create RegisterPanel
		registerPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, registerPanel, 400, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, registerPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, registerPanel, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, registerPanel, 0, SpringLayout.EAST, panel);
		panel.add(registerPanel);

		// Initialize RegisterArea.
		initRegisterArea();

	}

	/**
	 * Initialize SearchArea.
	 */
	@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
	private void initSearchArea() {

		// Create SpringLayout
		SpringLayout sl_panel_1 = new SpringLayout();
		searchPanel.setLayout(sl_panel_1);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		sl_panel_1.putConstraint(SpringLayout.NORTH, scrollPane, 45, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.WEST, scrollPane, 40, SpringLayout.WEST, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane, 345, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane, 540, SpringLayout.WEST, searchPanel);
		searchPanel.add(scrollPane);

		// TableInfo
		tblInfo = new JTable();
		tblInfo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"User Name", "User Type", "Nick Name"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		tblInfo.getColumnModel().getColumn(0).setPreferredWidth(100);
		tblInfo.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblInfo.getColumnModel().getColumn(2).setPreferredWidth(400);
		tblInfo.setEnabled(true);
		tblInfo.setColumnSelectionAllowed(false);
		tblInfo.setCellSelectionEnabled(false);
		tblInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblInfo.setRowSelectionAllowed(true);
		tblInfo.setBorder(new LineBorder(new Color(0, 0, 0)));
		tblInfo.getTableHeader().setBackground(new Color(153, 255, 204));
		tblInfo.getTableHeader().setFont(new Font("default", Font.BOLD, 13));
		scrollPane.setViewportView(tblInfo);

		// Initialize SearchArea's button
		initSearchAreaButton();

	}

	/**
	 * Initialize SearchArea's button
	 */
	private void initSearchAreaButton() {
		SpringLayout sl_panel = (SpringLayout) searchPanel.getLayout();

		// Button New
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actNew();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnNew, 360, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnNew, 40, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNew, 390, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNew, 120, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnNew);

		// Button Modify
		JButton btnModify = new JButton("Update");
		btnModify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actModify();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnModify, 360, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnModify, 130, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnModify, 390, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnModify, 210, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnModify);

		// Button Delete
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actDelete();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnDelete, 360, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnDelete, 220, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnDelete, 390, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnDelete, 300, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnDelete);

		// Button Register
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actRegister();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnRegister, 360, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnRegister, 350, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnRegister, 390, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnRegister, 450, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnRegister);

		// Button Back
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actBack();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnBack, 360, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnBack, 460, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnBack, 390, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnBack, 540, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnBack);
	}

	/**
	 * Initialize RegisterArea.
	 */
	@SuppressWarnings("rawtypes")
	private void initRegisterArea() {

		// Create SpringLayout
		SpringLayout sl_panel = new SpringLayout();
		registerPanel.setLayout(sl_panel);

		// Line separator
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		sl_panel.putConstraint(SpringLayout.NORTH, separator, 0, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, separator, 10, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, separator, 2, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, registerPanel);
		registerPanel.add(separator);

		// Label lblUserName
		JLabel lblUserName = new JLabel("User Name");
		sl_panel.putConstraint(SpringLayout.NORTH, lblUserName, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblUserName, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblUserName);

		// TextField txtUserName
		txtUserName = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtUserName, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtUserName, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtUserName, 430, SpringLayout.WEST, registerPanel);
		txtUserName.setDocument(new JTextFieldLimit(20));
		registerPanel.add(txtUserName);

		// Label lblPassword
		JLabel lblPassword = new JLabel("Password");
		sl_panel.putConstraint(SpringLayout.NORTH, lblPassword, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblPassword, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblPassword);

		// TextField txtPassword
		txtPassword = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtPassword, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtPassword, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtPassword, 430, SpringLayout.WEST, registerPanel);
		txtPassword.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtPassword);

		// Label lblUserType
		JLabel lblUserType = new JLabel("User Type");
		sl_panel.putConstraint(SpringLayout.NORTH, lblUserType, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblUserType, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblUserType);

		// Combobox cbxUserType
		cbxUserType = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, cbxUserType, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, cbxUserType, 130, SpringLayout.WEST, registerPanel);
		registerPanel.add(cbxUserType);

		// Label lblNickName
		JLabel lblNickName = new JLabel("Nick Name");
		sl_panel.putConstraint(SpringLayout.NORTH, lblNickName, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblNickName, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblNickName);

		// TextField NickName
		txtNickName = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtNickName, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtNickName, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtNickName, 430, SpringLayout.WEST, registerPanel);
		txtNickName.setDocument(new JTextFieldLimit(20));
		registerPanel.add(txtNickName);

		// Initialize RegisterArea's button
		initRegisterAreaButton();
	}

	/**
	 * Initialize RegisterArea's button
	 */
	private void initRegisterAreaButton() {
		SpringLayout sl_panel = (SpringLayout) registerPanel.getLayout();

		// Button OK
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actOK();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnOK, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnOK, 220, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOK, 170, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnOK, 300, SpringLayout.WEST, registerPanel);
		registerPanel.add(btnOK);

		// Button Cancel
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actCancel();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnCancel, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnCancel, 310, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnCancel, 170, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnCancel, 390, SpringLayout.WEST, registerPanel);
		registerPanel.add(btnCancel);

	}

	/**
	 * Initialize combobox
	 */
	@SuppressWarnings("unchecked")
	private void initComboBox() {

		// UserType
		for (UserType userType : UserType.values()) {
			ComboItem item =
					new ComboItem(
							userType.getCode(), userType.getLabel());
			cbxUserType.addItem(item);
		}

	}

	/**
	 * Initialize screen
	 */
	private void initScreen() {
		// Show data
		showData();

		// Enable SearchArea
		enableSearchArea();
	}

	/**
	 * Show table data
	 */
	private void showData() {
		userDtoList = new ArrayList<UserDto>();
		deletedList = new ArrayList<Long>();

		// Search all User
		userDtoList = UserService.searchUser(null);

		// Edit table's data
		editTableData(null);
	}

	/**
	 * Click button New
	 */
	private void actNew() {
		// Initial edit data
		editingUserDto = new UserDto();

		// Enable RegisterArea
		enableRegisterArea();

		editMode = EditMode.NEW;
	}

	/**
	 * Click button Update
	 */
	private void actModify() {
		// Check: is row selecting?
		int selectedIndex = tblInfo.getSelectedRow();
		if (selectedIndex < 0) {
			MessageUtil.showInfoMessage(frmUserMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Edit RegisterArea
		editingUserDto = userDtoList.get(selectedIndex).copy();
		txtUserName.setText(editingUserDto.getUserName());
		txtPassword.setText(editingUserDto.getPassword());
		ComponentUtil.selectItem(cbxUserType, editingUserDto.getUserType());
		txtNickName.setText(editingUserDto.getNickName());

		// Enable RegisterArea
		enableRegisterArea();

		editMode = EditMode.UPDATE;
	}

	/**
	 * Click button Delete
	 */
	private void actDelete() {
		// Check: is row selecting?
		int selectedIndex = tblInfo.getSelectedRow();
		if (selectedIndex < 0) {
			MessageUtil.showInfoMessage(frmUserMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmUserMaster, Constants.MSG_DELETE_CONFIRM)) {
			// Validate delete data
			if (!validateDeleteData()) {
				return;
			}

			// Add deleted data into deletedList
			Long userId = userDtoList.get(selectedIndex).getUserId();
			if (userId != null) {
				deletedList.add(userId);
			}

			// Remove deleted data
			userDtoList.remove(selectedIndex);

			// Edit table's data
			editTableData(null);
		}
	}

	/**
	 * Click button Register
	 */
	private void actRegister() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmUserMaster, Constants.MSG_REGISTER_CONFIRM)) {

			// Register all User
			if (!UserService.registerAll(userDtoList, deletedList)) {
				MessageUtil.showErrorMessage(frmUserMaster, Constants.MSG_REGISTER_FAIL);
				return;
			}

			// Show success message
			MessageUtil.showInfoMessage(frmUserMaster, Constants.MSG_REGISTER_SUCCESS);

			// Refresh Screen
			initScreen();
		}
	}

	/**
	 * Click button Back
	 */
	private void actBack() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmUserMaster, Constants.MSG_CANCEL_CONFIRM)) {
			frmUserMaster.dispose();
			frmParent.setVisible(true);
		}
	}

	/**
	 * Click button OK
	 */
	private void actOK() {
		// Validate register data
		if (!validateRegisterData()) {
			return;
		}

		ComboItem selectedUserType = (ComboItem) cbxUserType.getSelectedItem();

		// Edit SelectedData
		editingUserDto.setUserName(txtUserName.getText());
		editingUserDto.setPassword(txtPassword.getText());
		editingUserDto.setUserType(StringUtil.cnvToString(selectedUserType.getValue()));
		editingUserDto.setNickName(txtNickName.getText());
		editingUserDto.setIsChange(true);

		// Edit Data List
		Integer selectedIndex = null;
		if (EditMode.NEW.equals(editMode)) {
			// Mode New
			userDtoList.add(editingUserDto);
		} else {
			// Mode Modify
			selectedIndex = tblInfo.getSelectedRow();
			userDtoList.set(selectedIndex, editingUserDto);
		}

		// Edit table's data
		editTableData(selectedIndex);

		// Clear RegisterArea
		clearRegisterArea();

		// Enable SearchArea
		enableSearchArea();
	}

	/**
	 * Click button Cancel
	 */
	private void actCancel() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmUserMaster, Constants.MSG_CANCEL_CONFIRM)) {
			// Clear RegisterArea
			clearRegisterArea();

			// Enable SearchArea
			enableSearchArea();
		}
	}

	/**
	 * Edit table's data
	 *
	 * @param selectedIndex
	 */
	private void editTableData(Integer selectedIndex) {
		// Clear data
		DefaultTableModel tableModel  = (DefaultTableModel) tblInfo.getModel();
		tableModel.setRowCount(0);

		// Edit table's data from List
		for (UserDto userDto : userDtoList) {
			// Edit row data
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(userDto.getUserName());
			rowData.add(UserType.getUserType(userDto.getUserType()));
			rowData.add(userDto.getNickName());

			// Add row
			tableModel.addRow(rowData.toArray());
		}

		// Select row
		if (selectedIndex != null) {
			tblInfo.setRowSelectionInterval(selectedIndex, selectedIndex);
		}
	}

	/**
	 * Validate register data
	 *
	 * @return Boolean True:OK / False:Error
	 */
	private Boolean validateRegisterData() {
		// Word
		if (StringUtil.isNullOrEmpty(txtUserName.getText())) {
			MessageUtil.showErrorMessage(frmUserMaster, "Please input [Word].");
			return false;
		}

		// Meaning
		if (StringUtil.isNullOrEmpty(txtPassword.getText())) {
			MessageUtil.showErrorMessage(frmUserMaster, "Please input [Meaning].");
			return false;
		}

		return true;
	}

	/**
	 * Validate register data
	 *
	 * @return Boolean True:OK / False:Error
	 */
	private Boolean validateDeleteData() {
		return true;
	}

	/**
	 * Clear RegisterArea
	 */
	private void clearRegisterArea() {
		txtUserName.setText("");
		txtPassword.setText("");
		cbxUserType.setSelectedIndex(0);
		txtNickName.setText("");
		editMode = null;
	}

	/**
	 * Enable SearchArea
	 */
	private void enableSearchArea() {
		ComponentUtil.enableComponents(searchPanel, true);
		ComponentUtil.enableComponents(registerPanel, false);
		btnBack.setEnabled(true);
	}

	/**
	 * Enable RegisterArea
	 */
	private void enableRegisterArea() {
		ComponentUtil.enableComponents(searchPanel, false);
		ComponentUtil.enableComponents(registerPanel, true);
		btnBack.setEnabled(true);

		// Focus User Name
		txtUserName.requestFocus();
	}

	/**
	 * @return the frmUserMaster
	 */
	public JFrame getFrame() {
		return frmUserMaster;
	}
}