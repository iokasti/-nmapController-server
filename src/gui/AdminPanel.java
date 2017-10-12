package gui;

import static aggregatorManager.Main.agentActivationRequests;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.text.Document;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import org.jdesktop.swingx.JXDatePicker;

import Android.AndroidUser;
import agent.Agent;
import aggregatorManager.Main;
import database.DbOperations;
import nmapJob.Job;
import nmapJob.Result;

/**
 * The AdminPanel is a swing JFrame used as GUI for the aggregator manager.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
@SuppressWarnings("serial")
public class AdminPanel extends JFrame {

	private JPanel cardPanel, jp1, jp2, jp3, jp4, jp5, jp6, jp7, jp8, jp9, buttonPanel;
	private JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
	private CardLayout cardLayout = new CardLayout();
	private JList<Agent> jp1_dynamicAgentRequestList;
	private JList<AndroidUser> jp9_dynamicAndroidUsersActivationRequestList;
	private JList<Agent> jp3_dynamicActiveAgentsListForJobInsertion;
	private JList<Agent> jp7_dynamicActiveAgentsListForTermination;
	private JList<Agent> jp2_allAgentsDynamic;
	private JComboBox<Agent> jp6_agentsDynamicCB;
	private JComboBox<Agent> jp4_agentsDynamicCB;
	private JComboBox<Job> jp4_periodicJobsDynamicCB;
	private JEditorPane jp5_html, jp6_html;
	private JXDatePicker jp5_dateFrom, jp5_dateTo, jp6_dateFrom, jp6_dateTo;
	private JTextArea jp3_textArea;

	/**
	 * Admin panel constructor.
	 */
	public AdminPanel() {
		setTitle("Aggregator Manager Control Panel"); // set title for window
		setSize(1360, 768); // set window size
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // !! we must change
		setLocationRelativeTo(null);

		cardPanel = new JPanel(); // main panel
		buttonPanel = new JPanel(); // button panel

		cardPanel.setLayout(cardLayout); // main panel layout

		createRequestsPanel();
		createAgentsPanel();
		createInsertJobsPanel();
		createDeleteJobsPanel();
		createResultsPanel();
		createSaResultsPanel();
		createSaTerminationPanel();
		createAndroidRegisterPanel();
		createExitPanel();

		getContentPane().add(cardPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	/**
	 * Creates panel for agents info.
	 */
	private void createAgentsPanel() {
		jp2 = new JPanel();
		cardPanel.add(jp2, "2");

		// set up jpanel split
		JSplitPane jp2_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp2_splitPane.setResizeWeight(0.7);
		jp2_splitPane.setEnabled(false);
		jp2_splitPane.setDividerSize(0);

		// set up scroll pane
		JScrollPane jp2_scrollPane = new JScrollPane();
		jp2_scrollPane.setPreferredSize(new Dimension(1360, 640));

		// jpanel buttons for agents panel
		JPanel jp2_refreshButtonPanel = new JPanel();
		jp2_refreshButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// add scrollPane and requestButtonPanel to splitPane
		jp2_splitPane.add(jp2_scrollPane);
		jp2_splitPane.add(jp2_refreshButtonPanel);

		jp2.add(jp2_splitPane); // add splitPane to jp1

		jp2_allAgentsDynamic = new JList<Agent>();

		jp2_scrollPane.setViewportView(jp2_allAgentsDynamic);
		jp2_allAgentsDynamic.setBackground(UIManager.getColor("ColorChooser.swatchesDefaultRecentColor"));
		jp2_allAgentsDynamic.setBorder(null);
		jp2_allAgentsDynamic.setVisibleRowCount(-1);
		jp2.setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { jp2_scrollPane, jp1_dynamicAgentRequestList }));

		btn2 = new JButton("Agents");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "2");
				btn2.setBackground(new Color(72, 209, 204));
				btn1.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn8.setBackground(null);
				btn7.setBackground(null);
				btn9.setBackground(null);
			}
		});
		buttonPanel.add(btn2);

		JButton jp2_refresh = new JButton("Refresh");
		jp2_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<Agent> jp2_allAgents = new DefaultListModel<Agent>();
				ArrayList<Agent> jp2_Agents = null;
				try {
					jp2_Agents = DbOperations.getAllAgents();
					for (Agent agent : jp2_Agents) {
						jp2_allAgents.addElement(agent);
					}
					jp2_allAgentsDynamic.setModel(jp2_allAgents);
					jp2_allAgentsDynamic.clearSelection();
				} catch (SQLException e1) {
					System.err.println("A problem occured in sql.");
				}
			}
		});
		jp2_refreshButtonPanel.add(jp2_refresh);
	}

	/**
	 * Creates panel periodic job deletion.
	 */
	private void createDeleteJobsPanel() {
		jp4 = new JPanel();
		cardPanel.add(jp4, "4");

		jp4_agentsDynamicCB = new JComboBox<Agent>();
		jp4_agentsDynamicCB.setPreferredSize(new Dimension(1360 / 2, 50));

		jp4_periodicJobsDynamicCB = new JComboBox<Job>();
		jp4_periodicJobsDynamicCB.setPreferredSize(new Dimension(1360 / 2, 50));

		// jpanel
		JPanel jp4_selectionPanel = new JPanel();
		jp4_selectionPanel.setLayout(new GridLayout(3, 0, 0, 60));
		jp4_selectionPanel.setPreferredSize(new Dimension(1360 / 2, 320));

		jp4_selectionPanel.add(jp4_agentsDynamicCB);
		jp4_selectionPanel.add(jp4_periodicJobsDynamicCB);

		jp4.add(jp4_selectionPanel);

		btn4 = new JButton("Delete job");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "4");
				btn4.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn9.setBackground(null);
				btn1.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn7.setBackground(null);
				btn8.setBackground(null);

				jp4_agentsDynamicCB.removeAllItems();
				for (Agent jp4_agent : Collections.list(Main.activeAgents.elements())) {
					jp4_agentsDynamicCB.addItem(jp4_agent);
				}
				jp4_agentsDynamicCB.setSelectedItem(null);
				jp4_periodicJobsDynamicCB.setSelectedItem(null);
				jp4_periodicJobsDynamicCB.removeAllItems();
			}
		});
		buttonPanel.add(btn4);

		jp4_agentsDynamicCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jp4_periodicJobsDynamicCB.removeAllItems();
				if (jp4_agentsDynamicCB.getSelectedIndex() != -1) {
					Agent jp4_selectedAgent = (Agent) jp4_agentsDynamicCB.getSelectedItem();
					try {
						ArrayList<Job> jp4_periodicJobsRunning = Main.runningPeriodicJobs
								.get(Integer.valueOf(jp4_selectedAgent.getHash()));
						if (jp4_periodicJobsRunning != null && jp4_periodicJobsRunning.size() != 0) {
							for (Job jp4_job : jp4_periodicJobsRunning) {
								jp4_periodicJobsDynamicCB.addItem(jp4_job);
							}
						}
						jp4_periodicJobsDynamicCB.setSelectedItem(null);
					} catch (Exception e1) {
						// not an error
					}
				}
			}
		});

		JButton jp4_delete = new JButton("Delete selected periodic job");
		jp4_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected agent
				if (jp4_agentsDynamicCB.getSelectedIndex() != -1) {
					Agent jp4_selectedAgent = (Agent) jp4_agentsDynamicCB.getSelectedItem();
					if (jp4_selectedAgent != null) {
						if (Main.assignedJobs.containsKey(Integer.valueOf(jp4_selectedAgent.getHash())) == false) {
							Main.assignedJobs.put(Integer.valueOf(jp4_selectedAgent.getHash()), new ArrayList<Job>());
						}
						// get selected periodic job
						if (jp4_periodicJobsDynamicCB.getSelectedIndex() != -1) {
							Job job = (Job) jp4_periodicJobsDynamicCB.getSelectedItem();
							Main.runningPeriodicJobs.get(jp4_selectedAgent.getHash()).remove(job);

							job.setDelete();
							if (Main.assignedJobs.containsKey(Integer.valueOf(jp4_selectedAgent.getHash())) == false) {
								Main.assignedJobs.put(Integer.valueOf(jp4_selectedAgent.getHash()),
										new ArrayList<Job>());
							}
							Main.assignedJobs.get(Integer.valueOf(jp4_selectedAgent.getHash())).add(job);
						}
					}
				}
			}
		});
		jp4_selectionPanel.add(jp4_delete);
	}

	/**
	 * Creates panel for exiting the application.
	 */
	private void createExitPanel() {
		jp8 = new JPanel();
		cardPanel.add(jp8, "8");

		JSplitPane jp8_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp8_splitPane.setResizeWeight(0.5);
		jp8_splitPane.setEnabled(false);
		jp8_splitPane.setDividerSize(0);

		jp8.add(jp8_splitPane);

		btn8 = new JButton("Exit");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "8");
				btn8.setBackground(new Color(82, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn9.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn7.setBackground(null);
				btn1.setBackground(null);
			}
		});
		buttonPanel.add(btn8);

		JButton jp8_terminateAM = new JButton("Terminate aggregator manager");
		jp8_terminateAM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.server.shutdownNow();
				Main.checkForTimeOutThread.interrupt();
				try {
					Main.checkForTimeOutThread.join(1500);
				} catch (InterruptedException e1) {
					System.err.println("Could not join threads.");
				}
				try {
					DbOperations.setAllAgentsOffline();
				} catch (SQLException e1) {
					System.err.println("Could not set all agents offline.");

				}
				System.exit(0);
			}
		});
		jp8_splitPane.add(jp8_terminateAM);
	}

	/**
	 * Creates panel for job insertion.
	 */
	private void createInsertJobsPanel() {
		jp3 = new JPanel();
		jp3.setLayout(new GridLayout(0, 1, 0, 0));
		cardPanel.add(jp3, "3");

		// set up jpanel split
		JSplitPane jp3_MainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp3_MainSplitPane.setResizeWeight(0.8);
		jp3_MainSplitPane.setEnabled(false);
		jp3_MainSplitPane.setDividerSize(0);

		jp3_textArea = new JTextArea();
		JScrollPane jp3_TextAreaScrollPane = new JScrollPane(jp3_textArea);
		jp3_TextAreaScrollPane.setPreferredSize(new Dimension(1360 / 2, 640));

		jp3_dynamicActiveAgentsListForJobInsertion = new JList<Agent>();
		jp3_dynamicActiveAgentsListForJobInsertion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jp3_dynamicActiveAgentsListForJobInsertion.setModel(Main.activeAgents.getModel());
		jp3_dynamicActiveAgentsListForJobInsertion
				.setBackground(UIManager.getColor("ColorChooser.swatchesDefaultRecentColor"));
		jp3_dynamicActiveAgentsListForJobInsertion.setBorder(null);
		jp3_dynamicActiveAgentsListForJobInsertion.setVisibleRowCount(-1);

		jp3_dynamicActiveAgentsListForJobInsertion.clearSelection();
		JScrollPane jp3_ActiveAgentsScrollPane = new JScrollPane(jp3_dynamicActiveAgentsListForJobInsertion);
		jp3_ActiveAgentsScrollPane.setPreferredSize(new Dimension(1360 / 2, 640));

		JSplitPane jp3_topSplitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jp3_topSplitPane2.setEnabled(false);
		jp3_topSplitPane2.setResizeWeight(0.5);
		jp3_topSplitPane2.setDividerSize(0);

		jp3_topSplitPane2.add(jp3_TextAreaScrollPane);
		jp3_topSplitPane2.add(jp3_ActiveAgentsScrollPane);

		jp3_MainSplitPane.add(jp3_topSplitPane2);
		JPanel jp3_jobsButtonPanel = new JPanel();
		jp3_MainSplitPane.add(jp3_jobsButtonPanel);

		jp3.add(jp3_MainSplitPane); // add splitPane to jp2

		btn3 = new JButton("Insert jobs");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "3");
				btn3.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn1.setBackground(null);
				btn9.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn7.setBackground(null);
				btn8.setBackground(null);
			}
		});
		buttonPanel.add(btn3);

		JButton jp3_assign = new JButton("Assign jobs to selected Software Agent");
		jp3_assign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected agent
				Agent jp3_selectedAgent = jp3_dynamicActiveAgentsListForJobInsertion.getSelectedValue();
				if (jp3_selectedAgent != null) {
					if (Main.assignedJobs.containsKey(Integer.valueOf(jp3_selectedAgent.getHash())) == false) {
						Main.assignedJobs.put(Integer.valueOf(jp3_selectedAgent.getHash()), new ArrayList<Job>());
					}
					// get jobs from jp3_textArea line by line
					String[] jp3_jobs = jp3_textArea.getText().split("\\n");
					for (int i = 0; i < jp3_jobs.length; i++) {
						// get ArrayList with jobs for Agent with specific hash
						// and add the job
						Job jp3_job = new Job(jp3_jobs[i]);
						try {
							DbOperations.insertJob(jp3_job, jp3_selectedAgent.getHash());
						} catch (SQLException e1) {
							System.err.println("An error occured adding the job to the database");
						}
						Main.assignedJobs.get(Integer.valueOf(jp3_selectedAgent.getHash())).add(jp3_job);
					}
					jp3_textArea.setText("");
					jp3_dynamicActiveAgentsListForJobInsertion.clearSelection();
				}
			}
		});
		jp3_jobsButtonPanel.add(jp3_assign);

	}

	/**
	 * Creates panel for agents requests.
	 */
	private void createRequestsPanel() {
		jp1 = new JPanel();
		jp1.setLayout(new GridLayout(0, 1, 0, 0));
		cardPanel.add(jp1, "1");

		// set up jpanel split
		JSplitPane jp1_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp1_splitPane.setResizeWeight(0.7);
		jp1_splitPane.setEnabled(false);
		jp1_splitPane.setDividerSize(0);

		// set up scroll pane
		JScrollPane jp1_scrollPane = new JScrollPane();
		jp1_scrollPane.setPreferredSize(new Dimension(1360, 640));

		// button jpanel for request buttons
		JPanel jp1_requestButtonPanel = new JPanel();
		jp1_requestButtonPanel.setLayout(new GridLayout(2, 2, 0, 0));

		// add scrollPane and requestButtonPanel to splitPane
		jp1_splitPane.add(jp1_scrollPane);
		jp1_splitPane.add(jp1_requestButtonPanel);

		jp1.add(jp1_splitPane); // add splitPane to jp1

		jp1_dynamicAgentRequestList = new JList<Agent>();
		jp1_dynamicAgentRequestList.setModel(Main.agentActivationRequests.getModel());
		jp1_dynamicAgentRequestList.clearSelection();

		jp1_scrollPane.setViewportView(jp1_dynamicAgentRequestList);
		jp1_dynamicAgentRequestList.setBackground(UIManager.getColor("ColorChooser.swatchesDefaultRecentColor"));
		jp1_dynamicAgentRequestList.setBorder(null);
		jp1_dynamicAgentRequestList.setVisibleRowCount(-1);
		jp1.setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { jp1_scrollPane, jp1_dynamicAgentRequestList }));

		btn1 = new JButton("Requests");
		btn1.setBackground(new Color(72, 209, 204));
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "1");
				btn1.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn9.setBackground(null);
				btn7.setBackground(null);
			}
		});
		buttonPanel.add(btn1);

		JButton jp1_acceptAll = new JButton("Accept all requests");
		jp1_acceptAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get all agents 1 by 1 and accept them
				while (Main.agentActivationRequests.getSize() != 0) {
					for (int i = 0; i < Main.agentActivationRequests.getSize(); i++) {
						Agent jp1_agent = agentActivationRequests.get(i);
						jp1_agent.accept();
						Main.agentActivationRequests.remove(i);
					}
				}
				jp1_dynamicAgentRequestList.clearSelection();
			}
		});

		JButton jp1_acceptSelected = new JButton("Accept selected requests");
		jp1_acceptSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected agents 1 by 1 and accept them
				for (Agent jp1_agent : jp1_dynamicAgentRequestList.getSelectedValuesList()) {
					jp1_agent.accept();
					Main.agentActivationRequests.removeElement(jp1_agent);
				}
				jp1_dynamicAgentRequestList.clearSelection();
			}
		});

		JButton jp1_cancelAll = new JButton("Cancel all requests");
		jp1_cancelAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get all requests 1 by 1 and cancel them
				while (Main.agentActivationRequests.getSize() != 0) {
					for (int i = 0; i < Main.agentActivationRequests.getSize(); i++) {
						Agent jp1_agent = Main.agentActivationRequests.get(i);
						jp1_agent.reject();
						Main.agentActivationRequests.remove(i);
					}
				}
				jp1_dynamicAgentRequestList.clearSelection();
			}
		});

		JButton jp1_cancelSelected = new JButton("Cancel selected requests");
		jp1_cancelSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected requests cancel selected requests
				for (Agent jp1_agent : jp1_dynamicAgentRequestList.getSelectedValuesList()) {
					jp1_agent.reject();
					Main.agentActivationRequests.removeElement(jp1_agent);
				}
				jp1_dynamicAgentRequestList.clearSelection();
			}
		});

		// add selection buttons to splitPane
		jp1_requestButtonPanel.add(jp1_acceptAll);
		jp1_requestButtonPanel.add(jp1_acceptSelected);
		jp1_requestButtonPanel.add(jp1_cancelAll);
		jp1_requestButtonPanel.add(jp1_cancelSelected);
	}

	/**
	 * Creates panel for results.
	 */
	private void createResultsPanel() {
		jp5 = new JPanel();
		cardPanel.add(jp5, "5");

		// set up jpanel split
		JSplitPane jp5_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp5_splitPane.setResizeWeight(0.7);
		jp5_splitPane.setEnabled(false);
		jp5_splitPane.setDividerSize(0);

		// jpanel buttons
		JPanel jp5_datePanel = new JPanel();
		jp5_datePanel.setLayout(new GridLayout(1, 2, 0, 0));
		JPanel jp5_getResultsButtonPanel = new JPanel();
		jp5_getResultsButtonPanel.setLayout(new GridLayout(2, 1, 0, 0));

		// add date selection
		jp5_dateFrom = new JXDatePicker();
		jp5_dateFrom.setDate(Calendar.getInstance().getTime());
		jp5_dateFrom.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

		jp5_dateTo = new JXDatePicker();
		jp5_dateTo.setDate(Calendar.getInstance().getTime());
		jp5_dateTo.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

		jp5_datePanel.add(jp5_dateFrom);
		jp5_datePanel.add(jp5_dateTo);

		jp5_html = new JEditorPane();
		jp5_html.setEditable(false);

		// set up scroll pane
		JScrollPane jp5_scrollPane = new JScrollPane();
		jp5_scrollPane.setPreferredSize(new Dimension(1360, 640));
		jp5_scrollPane.getViewport().add(jp5_html);

		// add scrollPane and requestButtonPanel to splitPane
		jp5_splitPane.add(jp5_scrollPane);
		jp5_getResultsButtonPanel.add(jp5_datePanel);
		jp5_splitPane.add(jp5_getResultsButtonPanel);

		jp5.add(jp5_splitPane); // add splitPane to jp1

		btn5 = new JButton("Results");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "5");
				btn5.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn1.setBackground(null);
				btn6.setBackground(null);
				btn7.setBackground(null);
				btn8.setBackground(null);
				btn9.setBackground(null);

				Document doc = jp5_html.getDocument();
				doc.putProperty(Document.StreamDescriptionProperty, null);
			}
		});
		buttonPanel.add(btn5);

		JButton jp5_getResults = new JButton("Get results from database");
		jp5_getResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<Result> jp5_allResults = new DefaultListModel<Result>();
				ArrayList<Result> jp5_Results = null;
				try {
					jp5_Results = DbOperations.getAllResults();
					String htmlBody = "";
					for (Result jp5_result : jp5_Results) {
						if ((jp5_result.getDate().after(jp5_dateFrom.getDate())
								&& jp5_result.getDate().before(jp5_dateTo.getDate()))) {
							// check date selection bounds
							jp5_allResults.addElement(jp5_result);
							/** XML CONVERT TO HTML **/
							htmlBody += (getHTML(jp5_result.getResult())) + "\n";
							/*****************/
						}
					}
					createResultsHTML(htmlBody, "results.html");
					Document doc = jp5_html.getDocument();
					doc.putProperty(Document.StreamDescriptionProperty, null);
					jp5_html.setPage(new File("results.html").toURI().toURL());
				} catch (SQLException e1) {
					System.err.println("A problem occured in sql.");
				} catch (FileNotFoundException e1) {
					System.err.println("Problem in html.");
				} catch (UnsupportedEncodingException e1) {
					System.err.println("Problem in html.");
				} catch (IOException e1) {
					System.err.println("Problem in html.");
				}
			}
		});
		jp5_getResultsButtonPanel.add(jp5_getResults);
	}

	/**
	 * Creates panel for results from specific agent.
	 */
	private void createSaResultsPanel() {
		jp6 = new JPanel();
		cardPanel.add(jp6, "6");

		// set up jpanel split
		JSplitPane jp6_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp6_splitPane.setResizeWeight(0.6);
		jp6_splitPane.setEnabled(false);
		jp6_splitPane.setDividerSize(0);

		// jpanel buttons
		JPanel jp6_datePanel = new JPanel();
		jp6_datePanel.setLayout(new GridLayout(1, 2, 0, 0));
		JPanel jp6_getResultsButtonPanel = new JPanel();
		jp6_getResultsButtonPanel.setLayout(new GridLayout(2, 2, 0, 0));

		// add date selection
		jp6_dateFrom = new JXDatePicker();
		jp6_dateFrom.setDate(Calendar.getInstance().getTime());
		jp6_dateFrom.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

		jp6_dateTo = new JXDatePicker();
		jp6_dateTo.setDate(Calendar.getInstance().getTime());
		jp6_dateTo.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

		jp6_datePanel.add(jp6_dateFrom);
		jp6_datePanel.add(jp6_dateTo);

		jp6_html = new JEditorPane();
		jp6_html.setEditable(false);

		// set up scroll pane
		JScrollPane jp6_scrollPane = new JScrollPane();
		jp6_scrollPane.setPreferredSize(new Dimension(1360, 640));
		jp6_scrollPane.getViewport().add(jp6_html);

		// add scrollPane and requestButtonPanel to splitPane
		jp6_splitPane.add(jp6_scrollPane);
		jp6_getResultsButtonPanel.add(jp6_datePanel);
		jp6_splitPane.add(jp6_getResultsButtonPanel);

		jp6.add(jp6_splitPane); // add splitPane to jp1

		jp6_agentsDynamicCB = new JComboBox<Agent>();
		jp6_scrollPane.setColumnHeaderView(jp6_agentsDynamicCB);

		btn6 = new JButton("Agent Results");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "6");
				btn6.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn1.setBackground(null);
				btn8.setBackground(null);
				btn7.setBackground(null);
				btn9.setBackground(null);
				ArrayList<Agent> jp6_agents;
				try {
					jp6_agentsDynamicCB.removeAllItems();
					jp6_agents = DbOperations.getAllAgents();
					for (Agent jp6_agent : jp6_agents) {
						jp6_agentsDynamicCB.addItem(jp6_agent);
					}
				} catch (SQLException e1) {
					System.err.println("A problem occured in sql.");
				}
				jp6_agentsDynamicCB.setSelectedItem(null);
				Document doc = jp6_html.getDocument();
				doc.putProperty(Document.StreamDescriptionProperty, null);
			}
		});
		buttonPanel.add(btn6);

		JButton jp6_getResults = new JButton("Get results from database");
		jp6_getResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<Result> jp6_agentResults = new DefaultListModel<Result>();
				ArrayList<Result> jp6_Results = null;
				try {
					if (jp6_agentsDynamicCB.getSelectedIndex() != -1) {
						Agent jp6_selectedAgent = (Agent) jp6_agentsDynamicCB.getSelectedItem();
						jp6_Results = DbOperations.getAgentResults(jp6_selectedAgent.getHash());
						String htmlBody = "";
						for (Result jp6_result : jp6_Results) {
							if ((jp6_result.getDate().after(jp6_dateFrom.getDate())
									&& jp6_result.getDate().before(jp6_dateTo.getDate()))) {
								// check date selection bounds
								jp6_agentResults.addElement(jp6_result);
								/** XML CONVERT TO HTML **/
								htmlBody += (getHTML(jp6_result.getResult())) + "\n";
								/*****************/
							}
						}
						createResultsHTML(htmlBody, "results.html");
						Document doc = jp6_html.getDocument();
						doc.putProperty(Document.StreamDescriptionProperty, null);
						jp6_html.setPage(new File("results.html").toURI().toURL());
					}
				} catch (SQLException e1) {
					System.err.println("A problem occured in sql.");
				} catch (FileNotFoundException e1) {
					System.err.println("Problem in html.");
				} catch (UnsupportedEncodingException e1) {
					System.err.println("Problem in html.");
				} catch (IOException e1) {
					System.err.println("Problem in html.");
				}
			}
		});
		jp6_getResultsButtonPanel.add(jp6_getResults);
	}

	/**
	 * Creates panel for agent termination.
	 */
	private void createSaTerminationPanel() {
		jp7 = new JPanel();
		cardPanel.add(jp7, "7");

		// set up jpanel split
		JSplitPane jp7_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp7_splitPane.setResizeWeight(0.7);
		jp7_splitPane.setEnabled(false);
		jp7_splitPane.setDividerSize(0);

		// set up scroll pane
		JScrollPane jp7_scrollPane = new JScrollPane();
		jp7_scrollPane.setPreferredSize(new Dimension(1360, 640));

		// button jpanel for request buttons
		JPanel jp7_removeButtonPanel = new JPanel();
		jp7_removeButtonPanel.setLayout(new GridLayout(1, 1, 0, 0));

		// add scrollPane and requestButtonPanel to splitPane
		jp7_splitPane.add(jp7_scrollPane);
		jp7_splitPane.add(jp7_removeButtonPanel);

		jp7.add(jp7_splitPane); // add splitPane to jp7

		jp7_dynamicActiveAgentsListForTermination = new JList<Agent>();
		jp7_dynamicActiveAgentsListForTermination.setModel(Main.activeAgents.getModel());
		jp7_dynamicActiveAgentsListForTermination.clearSelection();

		jp7_scrollPane.setViewportView(jp7_dynamicActiveAgentsListForTermination);
		jp7_dynamicActiveAgentsListForTermination
				.setBackground(UIManager.getColor("ColorChooser.swatchesDefaultRecentColor"));
		jp7_dynamicActiveAgentsListForTermination.setBorder(null);
		jp7_dynamicActiveAgentsListForTermination.setVisibleRowCount(-1);
		jp7.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { jp7_scrollPane, jp7_dynamicActiveAgentsListForTermination }));

		btn7 = new JButton("Terminate Agent");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "7");
				btn7.setBackground(new Color(72, 209, 204));
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn8.setBackground(null);
				btn1.setBackground(null);
				btn9.setBackground(null);
			}
		});
		buttonPanel.add(btn7);

		JButton jp7_terminate = new JButton("Terminate selected agents");
		jp7_terminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected agents
				for (Agent jp7_agent : jp7_dynamicActiveAgentsListForTermination.getSelectedValuesList()) {
					if (Main.assignedJobs.containsKey(Integer.valueOf(jp7_agent.getHash())) == false) {
						Main.assignedJobs.put(Integer.valueOf(jp7_agent.getHash()), new ArrayList<Job>());
					}
					Main.assignedJobs.get(Integer.valueOf(jp7_agent.getHash())).add(new Job(true));
				}
				jp7_dynamicActiveAgentsListForTermination.clearSelection();
			}
		});
		jp7_removeButtonPanel.add(jp7_terminate);

	}
	
	/**
	 * Creates panel for android registration reuqests.
	 */
	private void createAndroidRegisterPanel() {
		jp9 = new JPanel();
		jp9.setLayout(new GridLayout(0, 1, 0, 0));
		cardPanel.add(jp9, "9");

		// set up jpanel split
		JSplitPane jp9_splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		jp9_splitPane.setResizeWeight(0.7);
		jp9_splitPane.setEnabled(false);
		jp9_splitPane.setDividerSize(0);

		// set up scroll pane
		JScrollPane jp9_scrollPane = new JScrollPane();
		jp9_scrollPane.setPreferredSize(new Dimension(1360, 640));

		// button jpanel for buttons
		JPanel jp9_requestButtonPanel = new JPanel();
		jp9_requestButtonPanel.setLayout(new GridLayout(2, 2, 0, 0));

		// add scrollPane and requestButtonPanel to splitPane
		jp9_splitPane.add(jp9_scrollPane);
		jp9_splitPane.add(jp9_requestButtonPanel);

		jp9.add(jp9_splitPane); // add splitPane to jp1

		jp9_dynamicAndroidUsersActivationRequestList = new JList<AndroidUser>();
		jp9_dynamicAndroidUsersActivationRequestList.setModel(Main.androidUsersActivationRequests.getModel());
		jp9_dynamicAndroidUsersActivationRequestList.clearSelection();

		jp9_scrollPane.setViewportView(jp9_dynamicAndroidUsersActivationRequestList);
		jp9_dynamicAndroidUsersActivationRequestList.setBackground(UIManager.getColor("ColorChooser.swatchesDefaultRecentColor"));
		jp9_dynamicAndroidUsersActivationRequestList.setBorder(null);
		jp9_dynamicAndroidUsersActivationRequestList.setVisibleRowCount(-1);
		jp9.setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[] { jp9_scrollPane, jp9_dynamicAndroidUsersActivationRequestList }));

		btn9 = new JButton("Android Users");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "9");
				btn1.setBackground(null);
				btn2.setBackground(null);
				btn3.setBackground(null);
				btn4.setBackground(null);
				btn5.setBackground(null);
				btn6.setBackground(null);
				btn7.setBackground(null);
				btn9.setBackground(new Color(72, 209, 204));
			}
		});
		buttonPanel.add(btn9);

		JButton jp9_acceptAll = new JButton("Accept all users");
		jp9_acceptAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get all agents 1 by 1 and accept them
				while (Main.androidUsersActivationRequests.getSize() != 0) {
					for (int i = 0; i < Main.androidUsersActivationRequests.getSize(); i++) {
						AndroidUser jp9_user = Main.androidUsersActivationRequests.get(i);
						jp9_user.accept();
						Main.androidUsersActivationRequests.remove(i);
					}
				}
				jp9_dynamicAndroidUsersActivationRequestList.clearSelection();
			}
		});

		JButton jp9_acceptSelected = new JButton("Accept selected users");
		jp9_acceptSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected agents 1 by 1 and accept them
				for (AndroidUser jp9_user : jp9_dynamicAndroidUsersActivationRequestList.getSelectedValuesList()) {
					jp9_user.accept();
					Main.androidUsersActivationRequests.removeElement(jp9_user);
				}
				jp9_dynamicAndroidUsersActivationRequestList.clearSelection();
			}
		});

		JButton jp9_cancelAll = new JButton("Cancel all users");
		jp9_cancelAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get all requests 1 by 1 and cancel them
				while (Main.androidUsersActivationRequests.getSize() != 0) {
					for (int i = 0; i < Main.androidUsersActivationRequests.getSize(); i++) {
						AndroidUser jp9_user = Main.androidUsersActivationRequests.get(i);
						jp9_user.reject();
						Main.androidUsersActivationRequests.remove(i);
					}
				}
				jp9_dynamicAndroidUsersActivationRequestList.clearSelection();
			}
		});

		JButton jp9_cancelSelected = new JButton("Cancel selected users");
		jp9_cancelSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get selected requests cancel selected requests
				for (AndroidUser jp9_user : jp9_dynamicAndroidUsersActivationRequestList.getSelectedValuesList()) {
					jp9_user.reject();
					Main.androidUsersActivationRequests.removeElement(jp9_user);
				}
				jp9_dynamicAndroidUsersActivationRequestList.clearSelection();
			}
		});

		// add selection buttons to splitPane
		jp9_requestButtonPanel.add(jp9_acceptAll);
		jp9_requestButtonPanel.add(jp9_acceptSelected);
		jp9_requestButtonPanel.add(jp9_cancelAll);
		jp9_requestButtonPanel.add(jp9_cancelSelected);
	}

	/**
	 * Returns an xml string to html format. Uses exec with xsltproc. input
	 * string is written to a temp.xml file and output is written to a temp.html
	 * file. Output is read in a string. Then we keep only the text between the
	 * html body tags and return it after both temp files are deleted.
	 * 
	 */
	private String getHTML(String xml) throws IOException {
		PrintWriter writer = new PrintWriter("temp.xml", "UTF-8");
		writer.println(xml);
		writer.close();

		String command = "xsltproc temp.xml -o temp.html";
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((reader.readLine()) != null) {
		}

		String temp_result = "";
		temp_result = readFile("temp.html");

		String result = StringUtils.substringBetween(temp_result, "<body>", "</body>");

		File fxml = new File("temp.xml");
		File fhtml = new File("temp.html");
		fxml.delete();
		fhtml.delete();
		return result;
	}

	/**
	 * Reads a file in a string.
	 * 
	 * @param filename
	 * @return String containing all the file
	 */
	private String readFile(String filename) {
		File f = new File(filename);
		try {
			byte[] bytes = Files.readAllBytes(f.toPath());
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Creates a valid html file for output in a swing window.
	 * 
	 * @param body
	 * @param output
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void createResultsHTML(String body, String output)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(output, false));
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Nmap Scan Reports</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println(body);
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
	}
}
