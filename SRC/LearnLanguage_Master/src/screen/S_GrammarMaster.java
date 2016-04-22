package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import dto.GrammarDto;
import dto.LessonDto;
import enums.EditMode;
import enums.MoveType;
import services.GrammarService;
import services.LessonService;
import util.ComponentUtil;
import util.Constants;
import util.MessageUtil;
import util.StringUtil;

public class S_GrammarMaster {

	// Grammar's Frame
	private JFrame frmGrammarMaster;

	// Parent's Frame
	private JFrame frmParent;

	// Panel SearchArea
	private JPanel searchPanel;
	// Panel RegisterArea
	private JPanel registerPanel;

	// Combobox Lesson
	@SuppressWarnings("rawtypes")
	private JComboBox cbxLesson;

	// Table info
	private JTable tblInfo;

	// TextField Syntax
	private JTextField txtSyntax;
	// TextField EXPLAIN
	private JTextField txtExplain;
	// TextField EXAMPLE
	private JTextField txtExample;

	// Button Back
	private JButton btnBack;

	// Data List
	private List<GrammarDto> grammarDtoList;
	// Deleted Data List
	private List<Long> deletedList;
	// Editing data
	private GrammarDto editingGrammarDto;

	// Edit mode
	private EditMode editMode;

	/**
	 * Create Grammar screen.
	 *
	 * @param frmParent
	 */
	public S_GrammarMaster(JFrame frmParent) {
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
		frmGrammarMaster = new JFrame();
		frmGrammarMaster.setResizable(false);
		frmGrammarMaster.setTitle("Grammar Master");
		frmGrammarMaster.setBounds(100, 100, 600, 580);
		frmGrammarMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGrammarMaster.getContentPane().setLayout(new BoxLayout(frmGrammarMaster.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmGrammarMaster.getContentPane().add(panel);
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

		// Label lblLesson
		JLabel lblLesson = new JLabel("Lesson");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblLesson, 15, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblLesson, 40, SpringLayout.WEST, searchPanel);
		searchPanel.add(lblLesson);

		// Combobox cbxLesson
		cbxLesson = new JComboBox();
		cbxLesson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showData();
			}
		});
		sl_panel_1.putConstraint(SpringLayout.NORTH, cbxLesson, 10, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.WEST, cbxLesson, 130, SpringLayout.WEST, searchPanel);
		searchPanel.add(cbxLesson);

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
				"Syntax", "Explain", "Example"
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
		tblInfo.getColumnModel().getColumn(0).setPreferredWidth(150);
		tblInfo.getColumnModel().getColumn(1).setPreferredWidth(150);
		tblInfo.getColumnModel().getColumn(2).setPreferredWidth(200);
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

		// Button MoveTop
		JButton btnMoveTop = new JButton("");
		btnMoveTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actMoveRow(MoveType.MOVE_TOP);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnMoveTop, 90, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnMoveTop, 14, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnMoveTop, 110, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnMoveTop, 34, SpringLayout.WEST, searchPanel);
		btnMoveTop.setIcon(new ImageIcon("resource/gif/double-up.gif"));
		searchPanel.add(btnMoveTop);

		// Button MoveUp
		JButton btnMoveUp = new JButton("");
		btnMoveUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actMoveRow(MoveType.MOVE_UP);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnMoveUp, 115, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnMoveUp, 14, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnMoveUp, 135, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnMoveUp, 34, SpringLayout.WEST, searchPanel);
		btnMoveUp.setIcon(new ImageIcon("resource/gif/up.gif"));
		searchPanel.add(btnMoveUp);

		// Button MoveDown
		JButton btnMoveDown = new JButton("");
		btnMoveDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actMoveRow(MoveType.MOVE_DOWN);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnMoveDown, 140, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnMoveDown, 14, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnMoveDown, 160, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnMoveDown, 34, SpringLayout.WEST, searchPanel);
		btnMoveDown.setIcon(new ImageIcon("resource/gif/down.gif"));
		searchPanel.add(btnMoveDown);

		// Button MoveLast
		JButton btnMoveLast = new JButton("");
		btnMoveLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actMoveRow(MoveType.MOVE_LAST);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnMoveLast, 165, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnMoveLast, 14, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnMoveLast, 185, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnMoveLast, 34, SpringLayout.WEST, searchPanel);
		btnMoveLast.setIcon(new ImageIcon("resource/gif/double-down.gif"));
		searchPanel.add(btnMoveLast);

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

		// Label lblSyntax
		JLabel lblSyntax = new JLabel("Syntax");
		sl_panel.putConstraint(SpringLayout.NORTH, lblSyntax, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblSyntax, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblSyntax);

		// TextField txtSyntax
		txtSyntax = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtSyntax, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtSyntax, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtSyntax, 430, SpringLayout.WEST, registerPanel);
		txtSyntax.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtSyntax);

		// Label lblExplain
		JLabel lblExplain = new JLabel("Explain");
		sl_panel.putConstraint(SpringLayout.NORTH, lblExplain, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblExplain, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblExplain);

		// TextField txtExplain
		txtExplain = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtExplain, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtExplain, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtExplain, 430, SpringLayout.WEST, registerPanel);
		txtExplain.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtExplain);

		// Label lblExample
		JLabel lblExample = new JLabel("Example");
		sl_panel.putConstraint(SpringLayout.NORTH, lblExample, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblExample, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblExample);

		// TextField txtExample
		txtExample = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtExample, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtExample, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtExample, 430, SpringLayout.WEST, registerPanel);
		txtExample.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtExample);

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
		sl_panel.putConstraint(SpringLayout.NORTH, btnOK, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnOK, 220, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOK, 140, SpringLayout.NORTH, registerPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnCancel, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnCancel, 310, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnCancel, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnCancel, 390, SpringLayout.WEST, registerPanel);
		registerPanel.add(btnCancel);

	}

	/**
	 * Initialize combobox
	 */
	@SuppressWarnings("unchecked")
	private void initComboBox() {

		// Lesson
		List<LessonDto> lessonDtoList = LessonService.searchLesson(null);
		for (LessonDto lessonDto : lessonDtoList) {
			ComboItem item =
				new ComboItem(
					lessonDto.getLessonId(), lessonDto.getLessonName());
			cbxLesson.addItem(item);
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
		grammarDtoList = new ArrayList<GrammarDto>();
		deletedList = new ArrayList<Long>();

		// Search all Grammar
		ComboItem lessonItem = (ComboItem) cbxLesson.getSelectedItem();
		grammarDtoList = GrammarService.searchGrammar(lessonItem.getValue());

		// Edit table's data
		editTableData(null);
	}

	/**
	 * Move row
	 *
	 * @param moveType
	 */
	private void actMoveRow(MoveType moveType) {
		// Check: is row selecting?
		int selectedIndex = tblInfo.getSelectedRow();
		if (selectedIndex < 0) {
			MessageUtil.showInfoMessage(frmGrammarMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Check valid move
		// if the first row is chosen, not move top, move up.
		if (selectedIndex == 0
				&& (MoveType.MOVE_TOP.equals(moveType)
					|| MoveType.MOVE_UP.equals(moveType))) {
			return;
		}

		// if the last row is chosen, not move down, move last.
		if (selectedIndex == (grammarDtoList.size() - 1)
				&& (MoveType.MOVE_DOWN.equals(moveType)
				|| MoveType.MOVE_LAST.equals(moveType))) {
			return;
		}

		// Get selected row
		GrammarDto grammarDto = grammarDtoList.get(selectedIndex).copy();

		// Remove old row
		grammarDtoList.remove(selectedIndex);

		// Add new row
		if (MoveType.MOVE_TOP.equals(moveType)) {
			selectedIndex = 0;
		} else if (MoveType.MOVE_UP.equals(moveType)) {
			selectedIndex = selectedIndex - 1;
		} else if (MoveType.MOVE_DOWN.equals(moveType)) {
			selectedIndex = selectedIndex + 1;
		} else if (MoveType.MOVE_LAST.equals(moveType)) {
			selectedIndex = grammarDtoList.size();
		}
		grammarDtoList.add(selectedIndex, grammarDto);

		// Edit table's data
		editTableData(selectedIndex);

	}

	/**
	 * Click button New
	 */
	private void actNew() {
		// Initial edit data
		editingGrammarDto = new GrammarDto();

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
			MessageUtil.showInfoMessage(frmGrammarMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Edit RegisterArea
		editingGrammarDto = grammarDtoList.get(selectedIndex).copy();
		txtSyntax.setText(editingGrammarDto.getSyntax());
		txtExplain.setText(editingGrammarDto.getExplain());
		txtExample.setText(editingGrammarDto.getExample());

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
			MessageUtil.showInfoMessage(frmGrammarMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmGrammarMaster, Constants.MSG_DELETE_CONFIRM)) {
			// Validate delete data
			if (!validateDeleteData()) {
				return;
			}

			// Add deleted data into deletedList
			Long grammarId = grammarDtoList.get(selectedIndex).getGrammarId();
			if (grammarId != null) {
				deletedList.add(grammarId);
			}

			// Remove deleted data
			grammarDtoList.remove(selectedIndex);

			// Edit table's data
			editTableData(null);
		}
	}

	/**
	 * Click button Register
	 */
	private void actRegister() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmGrammarMaster, Constants.MSG_REGISTER_CONFIRM)) {

			// Get selected lesson
			ComboItem lessonItem = (ComboItem) cbxLesson.getSelectedItem();

			// Register all Grammar
			if (!GrammarService.registerAll(grammarDtoList,
											   deletedList,
											   lessonItem.getValue())) {
				MessageUtil.showErrorMessage(frmGrammarMaster, Constants.MSG_REGISTER_FAIL);
				return;
			}

			// Show success message
			MessageUtil.showInfoMessage(frmGrammarMaster, Constants.MSG_REGISTER_SUCCESS);

			// Refresh Screen
			initScreen();
		}
	}

	/**
	 * Click button Back
	 */
	private void actBack() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmGrammarMaster, Constants.MSG_CANCEL_CONFIRM)) {
			frmGrammarMaster.dispose();
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

		// Edit SelectedData
		editingGrammarDto.setSyntax(txtSyntax.getText());
		editingGrammarDto.setExplain(txtExplain.getText());
		editingGrammarDto.setExample(txtExample.getText());
		editingGrammarDto.setIsChange(true);

		// Edit Data List
		Integer selectedIndex = null;
		if (EditMode.NEW.equals(editMode)) {
			// Mode New
			grammarDtoList.add(editingGrammarDto);
		} else {
			// Mode Modify
			selectedIndex = tblInfo.getSelectedRow();
			grammarDtoList.set(selectedIndex, editingGrammarDto);
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
		if (MessageUtil.showConfirmMessage(frmGrammarMaster, Constants.MSG_CANCEL_CONFIRM)) {
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
		for (GrammarDto grammarDto : grammarDtoList) {
			// Edit row data
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(grammarDto.getSyntax());
			rowData.add(grammarDto.getExplain());
			rowData.add(grammarDto.getExample());

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
		// Syntax
		if (StringUtil.isNullOrEmpty(txtSyntax.getText())) {
			MessageUtil.showErrorMessage(frmGrammarMaster, "Please input [Syntax].");
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
		txtSyntax.setText("");
		txtExplain.setText("");
		txtExample.setText("");
		txtExplain.setText("");
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

		// Focus Grammar Name
		txtSyntax.requestFocus();
	}

	/**
	 * @return the frmGrammarMaster
	 */
	public JFrame getFrame() {
		return frmGrammarMaster;
	}
}