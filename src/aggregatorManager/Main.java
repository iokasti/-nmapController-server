package aggregatorManager;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import cache.SynchedArrayList_Agent;
import cache.SynchedArrayList_AndroidUser;
import cache.SynchedDefaultListModel_Agent;
import cache.SynchedDefaultListModel_AndroidUser;
import cache.SynchedMap_Int_Job;
import database.DbInit;
import gui.AdminPanel;
import gui.LoginDialog;

public class Main {

	/** SHARED VARIABLES **/
	public static SynchedDefaultListModel_Agent agentActivationRequests;
	public static SynchedDefaultListModel_Agent activeAgents;
	public static SynchedArrayList_Agent agentsToBeAccepted;
	public static SynchedArrayList_Agent agentsToBeCanceled;
	public static SynchedMap_Int_Job assignedJobs;
	public static SynchedMap_Int_Job runningPeriodicJobs;
	public static Thread checkForTimeOutThread;
	public static HttpServer server;

	public static SynchedDefaultListModel_AndroidUser androidUsersActivationRequests;
	public static SynchedArrayList_AndroidUser androidUsersToBeAccepted;
	public static SynchedArrayList_AndroidUser androidUsersToBeCanceled;
	/******************************/

	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://0.0.0.0:4445/manager/";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		final ResourceConfig rc = new ResourceConfig().packages("aggregatorManager");
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/** INITIALIZE DATABASE INFO **/
		DbInit.initialize();
		/****************************/

		/** LOGIN DIALOG **/
		@SuppressWarnings("unused")
		LoginDialog logindialog = new LoginDialog();
		/******************************/

		/** READ PROPERTIES FROM PROPERTY FILE **/
		Properties.setProperties("Properties");
		/****************************/

		/** INITIALIZE CACHE STRUCTURES **/
		agentActivationRequests = new SynchedDefaultListModel_Agent();
		activeAgents = new SynchedDefaultListModel_Agent();
		agentsToBeAccepted = new SynchedArrayList_Agent();
		agentsToBeCanceled = new SynchedArrayList_Agent();
		assignedJobs = new SynchedMap_Int_Job();
		runningPeriodicJobs = new SynchedMap_Int_Job();
		androidUsersActivationRequests = new SynchedDefaultListModel_AndroidUser();
		androidUsersToBeAccepted = new SynchedArrayList_AndroidUser();
		androidUsersToBeCanceled = new SynchedArrayList_AndroidUser();
		/****************************/

		/** THREAD CHECK ACTIVE AGENTS FOR TIMEOUT **/
		CheckForTimeOut checkForTimeOut = new CheckForTimeOut();
		checkForTimeOutThread = new Thread(checkForTimeOut);
		checkForTimeOutThread.start();
		/**************************/

		server = startServer();
		System.err
				.println(String.format("Jersey app started with WADL available at " + "%sapplication.wadl", BASE_URI));

		/** ADMINISTRATION PANEL **/
		AdminPanel frame = new AdminPanel();
		/******************************/

		while (true) {
			System.out.println("Press any key to show the administration panel if it's hidden.");
			System.in.read();
			frame.setVisible(true);
		}
	}

}
