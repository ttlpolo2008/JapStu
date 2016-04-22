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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import component.JTextFieldLimit;
import dto.LessonDto;
import enums.EditMode;
import enums.MoveType;
import services.ExerciseService;
import services.LessonCourseService;
import services.LessonService;
import util.ComponentUtil;
import util.Constants;
import util.MessageUtil;
import util.StringUtil;

public class S_LessonMaster {

	// Lesson's Frame
	private JFrame frmLessonMaster;

	// Parent's Frame
	private JFrame frmParent;

	// Panel SearchArea
	private JPanel searchPanel;
	// Panel RegisterArea
	private JPanel registerPanel;

	// Table info
	private JTable tblInfo;

	// TextField LessonName
	private JTextField txtLessonName;
	// TextField PassScore
	private JTextField txtPassScore;

	// Button Back
	private JButton btnBack;

	// Data List
	private List<LessonDto> lessonDtoList;
	// Deleted Data List
	private List<Long> deletedList;
	// Editing data
	private LessonDto editingLessonDto;

	// Edit mode
	private EditMode editMode;

	/**
	 * Create Lesson screen.
	 *
	 * @param frmParent
	 */
	public S_LessonMaster(JFrame frmParent) {
		this.frmParent = frmParent;

		// Initialize Components
		initComponents();

		// Initialize screen
		initScreen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frmLessonMaster = new JFrame();
		frmLessonMaster.setResizable(false);
		frmLessonMaster.setTitle("Lesson Master");
		frmLessonMaster.setBounds(100, 100, 600, 560);
		frmLessonMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLessonMaster.getContentPane().setLayout(new BoxLayout(frmLessonMaster.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmLessonMaster.getContentPane().add(panel);
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
		SpringLayout sl_panel = new SpringLayout();
		searchPanel.setLayout(sl_panel);

		// Label lblLessonInfo
		JLabel lblLessonInfo = new JLabel("Lesson Info");
		sl_panel.putConstraint(SpringLayout.NORTH, lblLessonInfo, 20, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblLessonInfo, 40, SpringLayout.WEST, searchPanel);
		searchPanel.add(lblLessonInfo);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 45, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 40, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, 345, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 540, SpringLayout.WEST, searchPanel);
		searchPanel.add(scrollPane);

		// TableInfo
		tblInfo = new JTable();
		tblInfo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Lesson Name", "Pass Score"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class
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
		tblInfo.getColumnModel().getColumn(0).setPreferredWidth(400);
		tblInfo.getColumnModel().getColumn(1).setPreferredWidth(100);
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

		// Label lblLessonName
		JLabel lblLessonName = new JLabel("Lesson Name");
		sl_panel.putConstraint(SpringLayout.NORTH, lblLessonName, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblLessonName, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblLessonName);

		// TextField txtLessonName
		txtLessonName = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtLessonName, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtLessonName, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtLessonName, 430, SpringLayout.WEST, registerPanel);
		txtLessonName.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtLessonName);

		// Label lblPassScore
		JLabel lblPassScore = new JLabel("Pass Score");
		sl_panel.putConstraint(SpringLayout.NORTH, lblPassScore, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblPassScore, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblPassScore);

		// TextField txtPassScore
		txtPassScore = new JTextField();
		txtPassScore.setHorizontalAlignment(SwingConstants.RIGHT);
		sl_panel.putConstraint(SpringLayout.NORTH, txtPassScore, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtPassScore, 130, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtPassScore, 160, SpringLayout.WEST, registerPanel);
		txtPassScore.setDocument(new JTextFieldLimit(3));
		registerPanel.add(txtPassScore);

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
		sl_panel.putConstraint(SpringLayout.NORTH, btnOK, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnOK, 220, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOK, 110, SpringLayout.NORTH, registerPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnCancel, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnCancel, 310, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnCancel, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnCancel, 390, SpringLayout.WEST, registerPanel);
		registerPanel.add(btnCancel);

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
		lessonDtoList = new ArrayList<LessonDto>();
		deletedList = new ArrayList<Long>();

		// Search all Lesson
		lessonDtoList = LessonService.searchLesson(new LessonDto());

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
			MessageUtil.showInfoMessage(frmLessonMaster, Constants.MSG_NOT_CHOOSE_ROW);
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
		if (selectedIndex == (lessonDtoList.size() - 1)
				&& (MoveType.MOVE_DOWN.equals(moveType)
				|| MoveType.MOVE_LAST.equals(moveType))) {
			return;
		}

		// Get selected row
		LessonDto lessonDto = lessonDtoList.get(selectedIndex).copy();

		// Remove old row
		lessonDtoList.remove(selectedIndex);

		// Add new row
		if (MoveType.MOVE_TOP.equals(moveType)) {
			selectedIndex = 0;
		} else if (MoveType.MOVE_UP.equals(moveType)) {
			selectedIndex = selectedIndex - 1;
		} else if (MoveType.MOVE_DOWN.equals(moveType)) {
			selectedIndex = selectedIndex + 1;
		} else if (MoveType.MOVE_LAST.equals(moveType)) {
			selectedIndex = lessonDtoList.size();
		}
		lessonDtoList.add(selectedIndex, lessonDto);

		// Edit table's data
		editTableData(selectedIndex);

	}

	/**
	 * Click button New
	 */
	private void actNew() {
		// Initial edit data
		editingLessonDto = new LessonDto();

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
			MessageUtil.showInfoMessage(frmLessonMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Edit RegisterArea
		editingLessonDto = lessonDtoList.get(selectedIndex).copy();
		txtLessonName.setText(editingLessonDto.getLessonName());
		txtPassScore.setText(StringUtil.cnvToString(editingLessonDto.getPassScore()));

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
			MessageUtil.showInfoMessage(frmLessonMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmLessonMaster, Constants.MSG_DELETE_CONFIRM)) {
			// Validate delete data
			if (!validateDeleteData()) {
				return;
			}

			// Add deleted data into deletedList
			Long lessonId = lessonDtoList.get(selectedIndex).getLessonId();
			if (lessonId != null) {
				deletedList.add(lessonId);
			}

			// Remove deleted data
			lessonDtoList.remove(selectedIndex);

			// Edit table's data
			editTableData(null);
		}
	}

	/**
	 * Click button Register
	 */
	private void actRegister() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmLessonMaster, Constants.MSG_REGISTER_CONFIRM)) {
			// Register all Lesson
			if (!LessonService.registerAll(lessonDtoList, deletedList)) {
				MessageUtil.showErrorMessage(frmLessonMaster, Constants.MSG_REGISTER_FAIL);
				return;
			}

			// Show success message
			MessageUtil.showInfoMessage(frmLessonMaster, Constants.MSG_REGISTER_SUCCESS);

			// Refresh Screen
			initScreen();
		}
	}

	/**
	 * Click button Back
	 */
	private void actBack() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmLessonMaster, Constants.MSG_CANCEL_CONFIRM)) {
			frmLessonMaster.dispose();
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
		editingLessonDto.setLessonName(txtLessonName.getText());
		editingLessonDto.setPassScore(StringUtil.cnvToInt(txtPassScore.getText()));
		editingLessonDto.setIsChange(true);

		// Edit Data List
		Integer selectedIndex = null;
		if (EditMode.NEW.equals(editMode)) {
			// Mode New
			lessonDtoList.add(editingLessonDto);
		} else {
			// Mode Modify
			selectedIndex = tblInfo.getSelectedRow();
			lessonDtoList.set(selectedIndex, editingLessonDto);
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
		if (MessageUtil.showConfirmMessage(frmLessonMaster, Constants.MSG_CANCEL_CONFIRM)) {
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
		for (LessonDto lessonDto : lessonDtoList) {
			// Edit row data
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(lessonDto.getLessonName());
			rowData.add(lessonDto.getPassScore());

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
		// Lesson Name
		if (StringUtil.isNullOrEmpty(txtLessonName.getText())) {
			MessageUtil.showErrorMessage(frmLessonMaster, "Please input [Lesson Name].");
			return false;
		}

		// Pass Score
		String passScore = txtPassScore.getText();
		if (!StringUtil.isNullOrEmpty(passScore)
				&& StringUtil.cnvToInt(passScore) == null) {
			MessageUtil.showErrorMessage(frmLessonMaster, "Please input number for [Pass Score].");
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
		// Check foreign key
		Long lessonId = lessonDtoList.get(tblInfo.getSelectedRow()).getLessonId();
		if (LessonCourseService.countLessonCourse(lessonId, null) > 0
				|| ExerciseService.countExercise(lessonId) > 0) {
			MessageUtil.showErrorMessage(frmLessonMaster, Constants.MSG_DELETE_FOREIGN_KEY);
			return false;
		}

		return true;
	}

	/**
	 * Clear RegisterArea
	 */
	private void clearRegisterArea() {
		txtLessonName.setText("");
		txtPassScore.setText("");
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

		// Focus Lesson Name
		txtLessonName.requestFocus();
	}

	/**
	 * @return the frmLessonMaster
	 */
	public JFrame getFrame() {
		return frmLessonMaster;
	}
}