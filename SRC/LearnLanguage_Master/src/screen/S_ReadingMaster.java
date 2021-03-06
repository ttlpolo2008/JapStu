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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import component.JTextFieldLimit;
import dto.ComboItem;
import dto.LessonDto;
import dto.ReadingDto;
import enums.EditMode;
import enums.MoveType;
import services.LessonService;
import services.ReadingService;
import util.ComponentUtil;
import util.Constants;
import util.MessageUtil;
import util.StringUtil;

public class S_ReadingMaster {

	// Reading's Frame
	private JFrame frmReadingMaster;

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

	// TextField Title
	private JTextField txtTitle;
	// TextField Content
	private JTextArea txtContent;
	// TextField ContentPron
	private JTextArea txtContentPron;
	// TextField ContentTranslate
	private JTextArea txtContentTranslate;

	// Button Back
	private JButton btnBack;

	// Data List
	private List<ReadingDto> readingDtoList;
	// Deleted Data List
	private List<Long> deletedList;
	// Editing data
	private ReadingDto editingReadingDto;

	// Edit mode
	private EditMode editMode;

	/**
	 * Create Reading screen.
	 *
	 * @param frmParent
	 */
	public S_ReadingMaster(JFrame frmParent) {
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
		frmReadingMaster = new JFrame();
		frmReadingMaster.setResizable(false);
		frmReadingMaster.setTitle("Reading Master");
		frmReadingMaster.setBounds(100, 100, 600, 630);
		frmReadingMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReadingMaster.getContentPane().setLayout(new BoxLayout(frmReadingMaster.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmReadingMaster.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		// Create SearchPanel
		searchPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, searchPanel, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, searchPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, searchPanel, 300, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, searchPanel, 0, SpringLayout.EAST, panel);
		panel.add(searchPanel);

		// Initialize SearchArea.
		initSearchArea();

		// Create RegisterPanel
		registerPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, registerPanel, 300, SpringLayout.NORTH, panel);
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
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane, 245, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane, 540, SpringLayout.WEST, searchPanel);
		searchPanel.add(scrollPane);

		// TableInfo
		tblInfo = new JTable();
		tblInfo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Title", "Content", "Content Pronunce", "Content Translate"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
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
		tblInfo.getColumnModel().getColumn(0).setPreferredWidth(80);
		tblInfo.getColumnModel().getColumn(1).setPreferredWidth(120);
		tblInfo.getColumnModel().getColumn(2).setPreferredWidth(120);
		tblInfo.getColumnModel().getColumn(3).setPreferredWidth(180);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnNew, 260, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnNew, 40, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNew, 290, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnModify, 260, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnModify, 130, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnModify, 290, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnDelete, 260, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnDelete, 220, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnDelete, 290, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnRegister, 260, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnRegister, 350, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnRegister, 290, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnBack, 260, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnBack, 460, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnBack, 290, SpringLayout.NORTH, searchPanel);
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

		// Label lblTitle
		JLabel lblTitle = new JLabel("Title");
		sl_panel.putConstraint(SpringLayout.NORTH, lblTitle, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblTitle, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblTitle);

		// TextField txtTitle
		txtTitle = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtTitle, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtTitle, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtTitle, 450, SpringLayout.WEST, registerPanel);
		txtTitle.setDocument(new JTextFieldLimit(30));
		registerPanel.add(txtTitle);

		// Label lblContent
		JLabel lblContent = new JLabel("Content");
		sl_panel.putConstraint(SpringLayout.NORTH, lblContent, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblContent, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblContent);

		// TextField txtContent
		txtContent = new JTextArea();
		JScrollPane contentScrollPane = new JScrollPane(txtContent);
		sl_panel.putConstraint(SpringLayout.NORTH, contentScrollPane, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, contentScrollPane, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, contentScrollPane, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, contentScrollPane, 450, SpringLayout.WEST, registerPanel);
		txtContent.setDocument(new JTextFieldLimit(500));
		registerPanel.add(contentScrollPane);

		// Label lblContentPron
		JLabel lblContentPron = new JLabel("Content Pronunce");
		sl_panel.putConstraint(SpringLayout.NORTH, lblContentPron, 120, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblContentPron, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblContentPron);

		// TextField txtContentPron
		txtContentPron = new JTextArea();
		JScrollPane contentPronScrollPane = new JScrollPane(txtContentPron);
		sl_panel.putConstraint(SpringLayout.NORTH, contentPronScrollPane, 120, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, contentPronScrollPane, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, contentPronScrollPane, 180, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, contentPronScrollPane, 450, SpringLayout.WEST, registerPanel);
		txtContentPron.setDocument(new JTextFieldLimit(500));
		registerPanel.add(contentPronScrollPane);

		// Label lblContentTranslate
		JLabel lblContentTranslate = new JLabel("Content Translate");
		sl_panel.putConstraint(SpringLayout.NORTH, lblContentTranslate, 190, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblContentTranslate, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblContentTranslate);

		// TextField txtContentTranslate
		txtContentTranslate = new JTextArea();
		JScrollPane contentTranslateScrollPane = new JScrollPane(txtContentTranslate);
		sl_panel.putConstraint(SpringLayout.NORTH, contentTranslateScrollPane, 190, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, contentTranslateScrollPane, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, contentTranslateScrollPane, 250, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, contentTranslateScrollPane, 450, SpringLayout.WEST, registerPanel);
		txtContentTranslate.setDocument(new JTextFieldLimit(500));
		registerPanel.add(contentTranslateScrollPane);

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
		sl_panel.putConstraint(SpringLayout.NORTH, btnOK, 260, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnOK, 220, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOK, 290, SpringLayout.NORTH, registerPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnCancel, 260, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnCancel, 310, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnCancel, 290, SpringLayout.NORTH, registerPanel);
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
		readingDtoList = new ArrayList<ReadingDto>();
		deletedList = new ArrayList<Long>();

		// Search all Reading
		ComboItem lessonItem = (ComboItem) cbxLesson.getSelectedItem();
		readingDtoList = ReadingService.searchReading(lessonItem.getValue());

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
			MessageUtil.showInfoMessage(frmReadingMaster, Constants.MSG_NOT_CHOOSE_ROW);
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
		if (selectedIndex == (readingDtoList.size() - 1)
				&& (MoveType.MOVE_DOWN.equals(moveType)
				|| MoveType.MOVE_LAST.equals(moveType))) {
			return;
		}

		// Get selected row
		ReadingDto readingDto = readingDtoList.get(selectedIndex).copy();

		// Remove old row
		readingDtoList.remove(selectedIndex);

		// Add new row
		if (MoveType.MOVE_TOP.equals(moveType)) {
			selectedIndex = 0;
		} else if (MoveType.MOVE_UP.equals(moveType)) {
			selectedIndex = selectedIndex - 1;
		} else if (MoveType.MOVE_DOWN.equals(moveType)) {
			selectedIndex = selectedIndex + 1;
		} else if (MoveType.MOVE_LAST.equals(moveType)) {
			selectedIndex = readingDtoList.size();
		}
		readingDtoList.add(selectedIndex, readingDto);

		// Edit table's data
		editTableData(selectedIndex);

	}

	/**
	 * Click button New
	 */
	private void actNew() {
		// Initial edit data
		editingReadingDto = new ReadingDto();

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
			MessageUtil.showInfoMessage(frmReadingMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Edit RegisterArea
		editingReadingDto = readingDtoList.get(selectedIndex).copy();
		txtTitle.setText(editingReadingDto.getTitle());
		txtContent.setText(editingReadingDto.getContent());
		txtContentPron.setText(editingReadingDto.getContentPron());
		txtContentTranslate.setText(editingReadingDto.getContentTranslate());

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
			MessageUtil.showInfoMessage(frmReadingMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmReadingMaster, Constants.MSG_DELETE_CONFIRM)) {
			// Validate delete data
			if (!validateDeleteData()) {
				return;
			}

			// Add deleted data into deletedList
			Long readingId = readingDtoList.get(selectedIndex).getReadingId();
			if (readingId != null) {
				deletedList.add(readingId);
			}

			// Remove deleted data
			readingDtoList.remove(selectedIndex);

			// Edit table's data
			editTableData(null);
		}
	}

	/**
	 * Click button Register
	 */
	private void actRegister() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmReadingMaster, Constants.MSG_REGISTER_CONFIRM)) {

			// Get selected lesson
			ComboItem lessonItem = (ComboItem) cbxLesson.getSelectedItem();

			// Register all Reading
			if (!ReadingService.registerAll(readingDtoList,
											   deletedList,
											   lessonItem.getValue())) {
				MessageUtil.showErrorMessage(frmReadingMaster, Constants.MSG_REGISTER_FAIL);
				return;
			}

			// Show success message
			MessageUtil.showInfoMessage(frmReadingMaster, Constants.MSG_REGISTER_SUCCESS);

			// Refresh Screen
			initScreen();
		}
	}

	/**
	 * Click button Back
	 */
	private void actBack() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmReadingMaster, Constants.MSG_CANCEL_CONFIRM)) {
			frmReadingMaster.dispose();
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
		editingReadingDto.setTitle(txtTitle.getText());
		editingReadingDto.setContent(txtContent.getText());
		editingReadingDto.setContentPron(txtContentPron.getText());
		editingReadingDto.setContentTranslate(txtContentTranslate.getText());
		editingReadingDto.setIsChange(true);

		// Edit Data List
		Integer selectedIndex = null;
		if (EditMode.NEW.equals(editMode)) {
			// Mode New
			readingDtoList.add(editingReadingDto);
		} else {
			// Mode Modify
			selectedIndex = tblInfo.getSelectedRow();
			readingDtoList.set(selectedIndex, editingReadingDto);
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
		if (MessageUtil.showConfirmMessage(frmReadingMaster, Constants.MSG_CANCEL_CONFIRM)) {
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
		for (ReadingDto readingDto : readingDtoList) {
			// Edit row data
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(readingDto.getTitle());
			rowData.add(readingDto.getContent());
			rowData.add(readingDto.getContentPron());
			rowData.add(readingDto.getContentTranslate());

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
		// Content
		if (StringUtil.isNullOrEmpty(txtContent.getText())) {
			MessageUtil.showErrorMessage(frmReadingMaster, "Please input [Content].");
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
		txtTitle.setText("");
		txtContent.setText("");
		txtContentPron.setText("");
		txtContentTranslate.setText("");
		txtContentPron.setText("");
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

		// Focus Title
		txtTitle.requestFocus();
	}

	/**
	 * @return the frmReadingMaster
	 */
	public JFrame getFrame() {
		return frmReadingMaster;
	}
}