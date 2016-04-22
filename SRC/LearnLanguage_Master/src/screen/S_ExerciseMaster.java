package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import dto.ExerciseDto;
import dto.LessonDto;
import enums.AnswerType;
import enums.EditMode;
import enums.QuestionType;
import services.ExerciseService;
import services.LessonService;
import util.ComponentUtil;
import util.Constants;
import util.MessageUtil;
import util.StringUtil;

public class S_ExerciseMaster {

	// Exercise's Frame
	private JFrame frmExerciseMaster;

	// Parent's Frame
	private JFrame frmParent;

	// Panel SearchArea
	private JPanel searchPanel;
	// Panel RegisterArea
	private JPanel registerPanel;

	// Combobox Lesson
	@SuppressWarnings("rawtypes")
	private JComboBox cbxLesson;
	// Combobox QuestionType
	@SuppressWarnings("rawtypes")
	private JComboBox cbxQuestionType;
	// Combobox AnswerType
	@SuppressWarnings("rawtypes")
	private JComboBox cbxAnswerType;

	// Checkbox Answer_1
	private JCheckBox chkAnswer1;
	// Checkbox Answer_2
	private JCheckBox chkAnswer2;
	// Checkbox Answer_3
	private JCheckBox chkAnswer3;
	// Checkbox Answer_4
	private JCheckBox chkAnswer4;
	// Checkbox Answer_5
	private JCheckBox chkAnswer5;

	// Table info
	private JTable tblInfo;

	// TextField QuestionContent
	private JTextField txtQuestionContent;
	// TextField QuestionContentFile
	private JTextField txtQuestionContentFile;
	// TextField Mark
	private JTextField txtMark;
	// TextField Time
	private JTextField txtTime;
	// TextField Answer_1
	private JTextField txtAnswer1;
	// TextField Answer_2
	private JTextField txtAnswer2;
	// TextField Answer_3
	private JTextField txtAnswer3;
	// TextField Answer_4
	private JTextField txtAnswer4;
	// TextField Answer_5
	private JTextField txtAnswer5;

	// Button Back
	private JButton btnBack;

	// Data List
	private List<ExerciseDto> exerciseDtoList;
	// Deleted Data List
	private List<Long> deletedList;
	// Editing data
	private ExerciseDto editingExerciseDto;

	// Edit mode
	private EditMode editMode;

	/**
	 * Create Exercise screen.
	 *
	 * @param frmParent
	 */
	public S_ExerciseMaster(JFrame frmParent) {
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
		frmExerciseMaster = new JFrame();
		frmExerciseMaster.setResizable(false);
		frmExerciseMaster.setTitle("Exercise Master");
		frmExerciseMaster.setBounds(100, 100, 700, 680);
		frmExerciseMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmExerciseMaster.getContentPane().setLayout(new BoxLayout(frmExerciseMaster.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		frmExerciseMaster.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		// Create SearchPanel
		searchPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, searchPanel, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, searchPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, searchPanel, 280, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, searchPanel, 0, SpringLayout.EAST, panel);
		panel.add(searchPanel);

		// Initialize SearchArea.
		initSearchArea();

		// Create RegisterPanel
		registerPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, registerPanel, 280, SpringLayout.NORTH, panel);
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

		// Label lblExercise
		JLabel lblExercise = new JLabel("Exercise");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblExercise, 15, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblExercise, 40, SpringLayout.WEST, searchPanel);
		searchPanel.add(lblExercise);

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
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane, 225, SpringLayout.NORTH, searchPanel);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane, 640, SpringLayout.WEST, searchPanel);
		searchPanel.add(scrollPane);

		// TableInfo
		tblInfo = new JTable();
		tblInfo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Question Type", "Question Content"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class
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
		tblInfo.getColumnModel().getColumn(1).setPreferredWidth(550);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnNew, 240, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnNew, 40, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNew, 270, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnModify, 240, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnModify, 130, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnModify, 270, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnDelete, 240, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnDelete, 220, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnDelete, 270, SpringLayout.NORTH, searchPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnRegister, 240, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnRegister, 450, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnRegister, 270, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnRegister, 550, SpringLayout.WEST, searchPanel);
		searchPanel.add(btnRegister);

		// Button Back
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actBack();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnBack, 240, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnBack, 560, SpringLayout.WEST, searchPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnBack, 270, SpringLayout.NORTH, searchPanel);
		sl_panel.putConstraint(SpringLayout.EAST, btnBack, 640, SpringLayout.WEST, searchPanel);
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

		// Label lblQuestionType
		JLabel lblQuestionType = new JLabel("Question Type");
		sl_panel.putConstraint(SpringLayout.NORTH, lblQuestionType, 20, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblQuestionType, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblQuestionType);

		// Combobox cbxQuestionType
		cbxQuestionType = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, cbxQuestionType, 15, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, cbxQuestionType, 150, SpringLayout.WEST, registerPanel);
		registerPanel.add(cbxQuestionType);

		// Combobox cbxAnswerType
		cbxAnswerType = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, cbxAnswerType, 15, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, cbxAnswerType, 270, SpringLayout.WEST, registerPanel);
		registerPanel.add(cbxAnswerType);

		// Label lblQuestionContent
		JLabel lblQuestionContent = new JLabel("Question Content");
		sl_panel.putConstraint(SpringLayout.NORTH, lblQuestionContent, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblQuestionContent, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblQuestionContent);

		// TextField txtQuestionContent
		txtQuestionContent = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtQuestionContent, 50, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtQuestionContent, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtQuestionContent, 450, SpringLayout.WEST, registerPanel);
		txtQuestionContent.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtQuestionContent);

		// Label lblQuestionContentFile
		JLabel lblQuestionContentFile = new JLabel("Content File");
		sl_panel.putConstraint(SpringLayout.NORTH, lblQuestionContentFile, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblQuestionContentFile, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblQuestionContentFile);

		// TextField txtQuestionContentFile
		txtQuestionContentFile = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtQuestionContentFile, 80, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtQuestionContentFile, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtQuestionContentFile, 450, SpringLayout.WEST, registerPanel);
		txtQuestionContentFile.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtQuestionContentFile);

		// Label lblMark
		JLabel lblMark = new JLabel("Mark");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMark, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblMark, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblMark);

		// TextField txtMark
		txtMark = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtMark, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtMark, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtMark, 180, SpringLayout.WEST, registerPanel);
		txtMark.setDocument(new JTextFieldLimit(3));
		registerPanel.add(txtMark);

		// Label lblTime
		JLabel lblTime = new JLabel("Time (Second)");
		sl_panel.putConstraint(SpringLayout.NORTH, lblTime, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblTime, 250, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblTime);

		// TextField txtTime
		txtTime = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtTime, 110, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtTime, 340, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtTime, 370, SpringLayout.WEST, registerPanel);
		txtTime.setDocument(new JTextFieldLimit(3));
		registerPanel.add(txtTime);

		// Label lblAnswerChoose
		JLabel lblAnswerChoose = new JLabel("Answer Choose");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswerChoose, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswerChoose, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswerChoose);

		// Checkbox Answer_1 -> Answer_5
		chkAnswer1 = new JCheckBox("Answer 1");
		sl_panel.putConstraint(SpringLayout.NORTH, chkAnswer1, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, chkAnswer1, 150, SpringLayout.WEST, registerPanel);
		registerPanel.add(chkAnswer1);
		chkAnswer2 = new JCheckBox("Answer 2");
		sl_panel.putConstraint(SpringLayout.NORTH, chkAnswer2, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, chkAnswer2, 250, SpringLayout.WEST, registerPanel);
		registerPanel.add(chkAnswer2);
		chkAnswer3 = new JCheckBox("Answer 3");
		sl_panel.putConstraint(SpringLayout.NORTH, chkAnswer3, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, chkAnswer3, 350, SpringLayout.WEST, registerPanel);
		registerPanel.add(chkAnswer3);
		chkAnswer4 = new JCheckBox("Answer 4");
		sl_panel.putConstraint(SpringLayout.NORTH, chkAnswer4, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, chkAnswer4, 450, SpringLayout.WEST, registerPanel);
		registerPanel.add(chkAnswer4);
		chkAnswer5 = new JCheckBox("Answer 5");
		sl_panel.putConstraint(SpringLayout.NORTH, chkAnswer5, 140, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, chkAnswer5, 550, SpringLayout.WEST, registerPanel);
		registerPanel.add(chkAnswer5);

		// Label lblAnswer1
		JLabel lblAnswer1 = new JLabel("Answer 1");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswer1, 170, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswer1, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswer1);

		// TextField txtAnswer1
		txtAnswer1 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtAnswer1, 170, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtAnswer1, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtAnswer1, 450, SpringLayout.WEST, registerPanel);
		txtAnswer1.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtAnswer1);

		// Label lblAnswer2
		JLabel lblAnswer2 = new JLabel("Answer 2");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswer2, 200, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswer2, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswer2);

		// TextField txtAnswer2
		txtAnswer2 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtAnswer2, 200, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtAnswer2, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtAnswer2, 450, SpringLayout.WEST, registerPanel);
		txtAnswer2.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtAnswer2);

		// Label lblAnswer3
		JLabel lblAnswer3 = new JLabel("Answer 3");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswer3, 230, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswer3, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswer3);

		// TextField txtAnswer3
		txtAnswer3 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtAnswer3, 230, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtAnswer3, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtAnswer3, 450, SpringLayout.WEST, registerPanel);
		txtAnswer3.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtAnswer3);

		// Label lblAnswer4
		JLabel lblAnswer4 = new JLabel("Answer 4");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswer4, 260, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswer4, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswer4);

		// TextField txtAnswer4
		txtAnswer4 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtAnswer4, 260, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtAnswer4, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtAnswer4, 450, SpringLayout.WEST, registerPanel);
		txtAnswer4.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtAnswer4);

		// Label lblAnswer5
		JLabel lblAnswer5 = new JLabel("Answer 5");
		sl_panel.putConstraint(SpringLayout.NORTH, lblAnswer5, 290, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, lblAnswer5, 40, SpringLayout.WEST, registerPanel);
		registerPanel.add(lblAnswer5);

		// TextField txtAnswer5
		txtAnswer5 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txtAnswer5, 290, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, txtAnswer5, 150, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.EAST, txtAnswer5, 450, SpringLayout.WEST, registerPanel);
		txtAnswer5.setDocument(new JTextFieldLimit(100));
		registerPanel.add(txtAnswer5);

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
		sl_panel.putConstraint(SpringLayout.NORTH, btnOK, 320, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnOK, 220, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnOK, 350, SpringLayout.NORTH, registerPanel);
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
		sl_panel.putConstraint(SpringLayout.NORTH, btnCancel, 320, SpringLayout.NORTH, registerPanel);
		sl_panel.putConstraint(SpringLayout.WEST, btnCancel, 310, SpringLayout.WEST, registerPanel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnCancel, 350, SpringLayout.NORTH, registerPanel);
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

		// QuestionType
		for (QuestionType questionType : QuestionType.values()) {
			ComboItem item =
					new ComboItem(
							questionType.getCode(), questionType.getLabel());
			cbxQuestionType.addItem(item);
		}

		// AnswerType
		for (AnswerType answerType : AnswerType.values()) {
			ComboItem item =
					new ComboItem(
							answerType.getCode(), answerType.getLabel());
			cbxAnswerType.addItem(item);
		}

	}

	/**
	 * Initialize screen
	 */
	private void initScreen() {
		// Show data
		showData();

		// Clear RegisterArea
		clearRegisterArea();

		// Enable SearchArea
		enableSearchArea();
	}

	/**
	 * Show table data
	 */
	private void showData() {
		exerciseDtoList = new ArrayList<ExerciseDto>();
		deletedList = new ArrayList<Long>();

		// Search all Exercise
		ComboItem exerciseItem = (ComboItem) cbxLesson.getSelectedItem();
		exerciseDtoList = ExerciseService.searchExercise(exerciseItem.getValue());

		// Edit table's data
		editTableData(null);
	}

	/**
	 * Click button New
	 */
	private void actNew() {
		// Initial edit data
		editingExerciseDto = new ExerciseDto();

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
			MessageUtil.showInfoMessage(frmExerciseMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Enable RegisterArea
		enableRegisterArea();

		// Edit RegisterArea
		editingExerciseDto = exerciseDtoList.get(selectedIndex).copy();
		ComponentUtil.selectItem(cbxQuestionType, editingExerciseDto.getQuestionType());
		ComponentUtil.selectItem(cbxAnswerType, editingExerciseDto.getAnswerType());
		txtQuestionContent.setText(editingExerciseDto.getQuestionContent());
		txtQuestionContentFile.setText(editingExerciseDto.getQuestionContentFile());
		txtMark.setText(StringUtil.cnvToString(editingExerciseDto.getMark()));
		txtTime.setText(StringUtil.cnvToString(editingExerciseDto.getTime()));
		if (!StringUtil.isNullOrEmpty(editingExerciseDto.getAnswerChoose())) {
			ComponentUtil.setCheckBoxValue(chkAnswer1, editingExerciseDto.getAnswerChoose().charAt(0));
			ComponentUtil.setCheckBoxValue(chkAnswer2, editingExerciseDto.getAnswerChoose().charAt(1));
			ComponentUtil.setCheckBoxValue(chkAnswer3, editingExerciseDto.getAnswerChoose().charAt(2));
			ComponentUtil.setCheckBoxValue(chkAnswer4, editingExerciseDto.getAnswerChoose().charAt(3));
			ComponentUtil.setCheckBoxValue(chkAnswer5, editingExerciseDto.getAnswerChoose().charAt(4));
		}
		txtAnswer1.setText(editingExerciseDto.getAnswer1());
		txtAnswer2.setText(editingExerciseDto.getAnswer2());
		txtAnswer3.setText(editingExerciseDto.getAnswer3());
		txtAnswer4.setText(editingExerciseDto.getAnswer4());
		txtAnswer5.setText(editingExerciseDto.getAnswer5());

		editMode = EditMode.UPDATE;
	}

	/**
	 * Click button Delete
	 */
	private void actDelete() {
		// Check: is row selecting?
		int selectedIndex = tblInfo.getSelectedRow();
		if (selectedIndex < 0) {
			MessageUtil.showInfoMessage(frmExerciseMaster, Constants.MSG_NOT_CHOOSE_ROW);
			return;
		}

		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmExerciseMaster, Constants.MSG_DELETE_CONFIRM)) {
			// Validate delete data
			if (!validateDeleteData()) {
				return;
			}

			// Add deleted data into deletedList
			Long exerciseId = exerciseDtoList.get(selectedIndex).getExerciseId();
			if (exerciseId != null) {
				deletedList.add(exerciseId);
			}

			// Remove deleted data
			exerciseDtoList.remove(selectedIndex);

			// Edit table's data
			editTableData(null);
		}
	}

	/**
	 * Click button Register
	 */
	private void actRegister() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmExerciseMaster, Constants.MSG_REGISTER_CONFIRM)) {

			// Get selected exercise
			ComboItem exerciseItem = (ComboItem) cbxLesson.getSelectedItem();

			// Register all Exercise
			if (!ExerciseService.registerAll(exerciseDtoList,
											   deletedList,
											   exerciseItem.getValue())) {
				MessageUtil.showErrorMessage(frmExerciseMaster, Constants.MSG_REGISTER_FAIL);
				return;
			}

			// Show success message
			MessageUtil.showInfoMessage(frmExerciseMaster, Constants.MSG_REGISTER_SUCCESS);

			// Refresh Screen
			initScreen();
		}
	}

	/**
	 * Click button Back
	 */
	private void actBack() {
		// Show confirm message
		if (MessageUtil.showConfirmMessage(frmExerciseMaster, Constants.MSG_CANCEL_CONFIRM)) {
			frmExerciseMaster.dispose();
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

		ComboItem selectedQuestionType = (ComboItem) cbxQuestionType.getSelectedItem();
		ComboItem selectedAnswerType = (ComboItem) cbxAnswerType.getSelectedItem();
		String answerChoose = ComponentUtil.getCheckBoxValue(chkAnswer1)
				+ ComponentUtil.getCheckBoxValue(chkAnswer2)
				+ ComponentUtil.getCheckBoxValue(chkAnswer3)
				+ ComponentUtil.getCheckBoxValue(chkAnswer4)
				+ ComponentUtil.getCheckBoxValue(chkAnswer5);

		// Edit SelectedData
		editingExerciseDto.setQuestionType(StringUtil.cnvToString(selectedQuestionType.getValue()));
		editingExerciseDto.setAnswerType(StringUtil.cnvToString(selectedAnswerType.getValue()));
		editingExerciseDto.setQuestionContent(txtQuestionContent.getText());
		editingExerciseDto.setQuestionContentFile(txtQuestionContentFile.getText());
		editingExerciseDto.setMark(StringUtil.cnvToInt(txtMark.getText()));
		editingExerciseDto.setTime(StringUtil.cnvToInt(txtTime.getText()));
		editingExerciseDto.setAnswerChoose(answerChoose);
		editingExerciseDto.setAnswer1(txtAnswer1.getText());
		editingExerciseDto.setAnswer2(txtAnswer2.getText());
		editingExerciseDto.setAnswer3(txtAnswer3.getText());
		editingExerciseDto.setAnswer4(txtAnswer4.getText());
		editingExerciseDto.setAnswer5(txtAnswer5.getText());
		editingExerciseDto.setIsChange(true);

		// Edit Data List
		Integer selectedIndex = null;
		if (EditMode.NEW.equals(editMode)) {
			// Mode New
			exerciseDtoList.add(editingExerciseDto);
		} else {
			// Mode Modify
			selectedIndex = tblInfo.getSelectedRow();
			exerciseDtoList.set(selectedIndex, editingExerciseDto);
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
		if (MessageUtil.showConfirmMessage(frmExerciseMaster, Constants.MSG_CANCEL_CONFIRM)) {
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
		for (ExerciseDto exerciseDto : exerciseDtoList) {
			// Edit row data
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(QuestionType.getQuestionType(exerciseDto.getQuestionType()));
			rowData.add(exerciseDto.getQuestionContent());

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
		// Question Content
		if (StringUtil.isNullOrEmpty(txtQuestionContent.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Question Content].");
			return false;
		}

		// Answer 1
		if (chkAnswer1.isSelected()
				&& StringUtil.isNullOrEmpty(txtAnswer1.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Answer 1].");
			return false;
		}

		// Answer 2
		if (chkAnswer2.isSelected()
				&& StringUtil.isNullOrEmpty(txtAnswer2.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Answer 2].");
			return false;
		}

		// Answer 3
		if (chkAnswer3.isSelected()
				&& StringUtil.isNullOrEmpty(txtAnswer3.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Answer 3].");
			return false;
		}

		// Answer 4
		if (chkAnswer4.isSelected()
				&& StringUtil.isNullOrEmpty(txtAnswer4.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Answer 4].");
			return false;
		}

		// Answer 5
		if (chkAnswer5.isSelected()
				&& StringUtil.isNullOrEmpty(txtAnswer5.getText())) {
			MessageUtil.showErrorMessage(frmExerciseMaster, "Please input [Answer 5].");
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
		cbxQuestionType.setSelectedIndex(0);
		cbxAnswerType.setSelectedIndex(0);
		txtQuestionContent.setText("");
		txtQuestionContentFile.setText("");
		txtMark.setText("");
		txtTime.setText("");
		chkAnswer1.setSelected(false);
		chkAnswer2.setSelected(false);
		chkAnswer3.setSelected(false);
		chkAnswer4.setSelected(false);
		chkAnswer5.setSelected(false);
		txtAnswer1.setText("");
		txtAnswer2.setText("");
		txtAnswer3.setText("");
		txtAnswer4.setText("");
		txtAnswer5.setText("");
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

		// Focus Exercise Name
		cbxQuestionType.requestFocus();
	}

	/**
	 * @return the frmExerciseMaster
	 */
	public JFrame getFrame() {
		return frmExerciseMaster;
	}
}