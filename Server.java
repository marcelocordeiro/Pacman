import java.net.*;
import java.io.*;

class Server
{
	static final int PORT = 2013;
	
	public static void main(String[] args) 
	{
		ServerSocket serverSocket=null;
		try 
		{
			serverSocket = new ServerSocket(PORT);
		} 
		catch (IOException e) 
		{
			System.out.println("Impossível acessar porta " + PORT + ", " + e);
      System.exit(1);
    }
    
		Socket clientSocket1 = null;
		try
		{
			System.out.println("Aguardando cliente 0");
			clientSocket1 = serverSocket.accept();
			PrintStream os1 = new PrintStream(new BufferedOutputStream(clientSocket1.getOutputStream(), 1024), true);
			os1.println(0);
		}
		catch (IOException e)
		{
			System.out.println("Accept falhou: Porta " + PORT + ", " + e);
			System.exit(1);
		}		
		System.out.println("Accept cliente 0");
		
		Socket clientSocket2 = null;
		try
		{
			System.out.println("Aguardando cliente 1");
			clientSocket2 = serverSocket.accept();
			PrintStream os2 = new PrintStream(new BufferedOutputStream(clientSocket2.getOutputStream(), 1024), true);
			os2.println(1);
		}
		catch (IOException e)
		{
			System.out.println("Accept falhou: Porta " + PORT + ", " + e);
			System.exit(1);
		}
		
		System.out.println("Accept cliente 1");
		
		new Serving(clientSocket1).start();
		new Serving(clientSocket2).start();
	}
}

class Serving extends Thread
{
	Socket clientSocket;
	int id_client;
	static PrintStream os[] = new PrintStream[2];
	static int cont=0;
	
	Serving(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		this.id_client = this.cont;
		this.cont++;
	}
	
	public void run()
	{
		int id_outro;
		int iXPac, iYPac, iPac;
		String come;
		if (id_client == 0)
			id_outro = 1;
		else
			id_outro = 0;
		System.out.println("Iniciando thread do cliente " + id_client);
		try
		{
			DataInputStream is = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			os[id_client] = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream(), 1024), true);
			
			do
			{
				try
				{
					iPac = Integer.parseInt(is.readLine());
					iXPac = Integer.parseInt(is.readLine());
					iYPac = Integer.parseInt(is.readLine());
					come = is.readLine();
					os[id_outro].println(iPac);
					os[id_outro].println(iXPac);
					os[id_outro].println(iYPac);
					os[id_outro].println(come);
					os[id_outro].flush();
				}catch (SocketException e)
				{
					System.exit(0);
				}
			} while(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
