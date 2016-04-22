package screen;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dao.CommonDAO;
import net.miginfocom.swing.MigLayout;
import util.MessageUtil;

public class S_Menu {

	private JFrame frmMenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					S_Menu window = new S_Menu();
					window.frmMenu.setVisible(true);

					// Connect DB
					if (!CommonDAO.connectDB()) {
						MessageUtil.showErrorMessage(window.frmMenu, "Can not connect DataBase");
						System.exit(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public S_Menu() {
		initComponents();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frmMenu = new JFrame();
		frmMenu.setTitle("Menu");
		frmMenu.setResizable(false);
		frmMenu.setBounds(100, 100, 500, 250);
		frmMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmMenu.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[50px][][500px][][500px][][][][][][89px][][]", "[20px][50px][][][][][]"));

		JButton btnUserMaster = new JButton("User Master");
		btnUserMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_UserMaster window = new S_UserMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnUserMaster, "cell 1 1,alignx left,aligny top");

		JButton btnLessonMaster = new JButton("Lesson Master");
		btnLessonMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_LessonMaster window = new S_LessonMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnLessonMaster, "cell 3 1,alignx left,aligny top");

		JButton btnVocabularyMaster = new JButton("Vocabulary Master");
		btnVocabularyMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_VocabularyMaster window = new S_VocabularyMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnVocabularyMaster, "cell 1 3,alignx left,aligny top");

		JButton btnKanjiMaster = new JButton("Kanji Master");
		btnKanjiMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_KanjiMaster window = new S_KanjiMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnKanjiMaster, "cell 3 3");

		JButton btnGrammarMaster = new JButton("Grammar Master");
		btnGrammarMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_GrammarMaster window = new S_GrammarMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnGrammarMaster, "cell 5 3");

		JButton btnReadingMaster = new JButton("Reading Master");
		btnReadingMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_ReadingMaster window = new S_ReadingMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnReadingMaster, "cell 1 5");

		JButton btnListeningMaster = new JButton("Listening Master");
		btnListeningMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_ListeningMaster window = new S_ListeningMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnListeningMaster, "cell 3 5");

		JButton btnConversationMaster = new JButton("Conversation Master");
		btnConversationMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_ConversationMaster window = new S_ConversationMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnConversationMaster, "cell 5 5");

		JButton btnExerciseMaster = new JButton("Exercise Master");
		btnExerciseMaster.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				S_ExerciseMaster window = new S_ExerciseMaster(frmMenu);
				window.getFrame().setVisible(true);
				frmMenu.setVisible(false);
			}
		});
		panel.add(btnExerciseMaster, "cell 1 7");

	}

}
