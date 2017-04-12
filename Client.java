import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import java.lang.String.*;
import java.lang.Integer.*;
import java.awt.Graphics;  
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

class Client extends JFrame implements Runnable
{
	static final int PORT = 2013; 
	static PrintStream os = null;
	static DataInputStream is = null;
	
	static final int  PAC1 = 0;
	static final int  PAC2D = 1;
	static final int  PAC2S = 2;
	static final int  PAC2A = 3;
	static final int  PAC2W = 4;
	static final int  PAC3D = 5;
	static final int  PAC3S = 6;
	static final int  PAC3A = 7;
	static final int  PAC3W = 8;
	
	static final int tam = 65;
	
	static int iPac1 = PAC2D;
	static int iPac2 = PAC2A;
	static int iXPac1 = 0;
	static int iYPac1 = 0;
	static int iXPac2 = 910;
	static int iYPac2 = 520;
	static int iPasso = 5;
	
	static int id_client;
	
	static Timer timer;
	
	boolean bVai = true;
	
	boolean dPac1 = true;
	boolean bPac2 = true;
	boolean ePac1 = true;
	boolean cPac1 = true;
	boolean bPac1 = true;
	
	boolean bCome1 = false;
	boolean bCome2 = false;
	
	static Image Pacs[] = new Image[9];
	static Image Come[] = new Image[9];
	static Image Princ1[] = new Image[9];
	static Image Princ2[] = new Image[9];
	static Image Cereja;

	class Pacman extends JPanel implements Runnable
	{
		int[][] mapa = //[15][9]
		{
			{0,0,0,0,0,0,0,1,2,0,0,0,0,0,0},
			{0,1,1,0,1,1,0,1,0,1,1,0,1,1,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,2,0,0},
			{0,1,1,0,1,0,1,1,1,1,1,0,1,1,0},
			{0,0,0,0,1,2,0,0,1,0,0,0,0,0,0},
			{0,1,1,0,1,1,1,0,1,0,1,1,0,1,0},
			{0,0,0,2,1,0,0,0,0,0,0,0,0,1,0},
			{0,1,0,0,0,0,1,1,0,1,0,1,1,1,0},
			{0,0,0,1,1,0,0,0,2,1,0,0,0,0,0}
		};
		
		Pacman()
		{
			try
			{
				Pacs[PAC1] = ImageIO.read(new File("images/pac1.png"));
				Pacs[PAC2D] = ImageIO.read(new File("images/pac2d.png"));
				Pacs[PAC2S] = ImageIO.read(new File("images/pac2s.png"));
				Pacs[PAC2A] = ImageIO.read(new File("images/pac2a.png"));
				Pacs[PAC2W] = ImageIO.read(new File("images/pac2w.png"));
				Pacs[PAC3D] = ImageIO.read(new File("images/pac3d.png"));
				Pacs[PAC3S] = ImageIO.read(new File("images/pac3s.png"));
				Pacs[PAC3A] = ImageIO.read(new File("images/pac3a.png"));
				Pacs[PAC3W] = ImageIO.read(new File("images/pac3w.png"));
				Come[PAC1] = ImageIO.read(new File("images/pac1c.png"));
				Come[PAC2D] = ImageIO.read(new File("images/pac2dc.png"));
				Come[PAC2S] = ImageIO.read(new File("images/pac2sc.png"));
				Come[PAC2A] = ImageIO.read(new File("images/pac2ac.png"));
				Come[PAC2W] = ImageIO.read(new File("images/pac2wc.png"));
				Come[PAC3D] = ImageIO.read(new File("images/pac3dc.png"));
				Come[PAC3S] = ImageIO.read(new File("images/pac3sc.png"));
				Come[PAC3A] = ImageIO.read(new File("images/pac3ac.png"));
				Come[PAC3W] = ImageIO.read(new File("images/pac3wc.png"));
				Cereja = ImageIO.read(new File("images/cereja.png"));
				Princ1 = Pacs;
				Princ2 = Pacs;
			} catch (IOException e) {}
			setVisible(true);
			setFocusable(true);
			requestFocusInWindow();
			new Thread(this).start();
		}
		
		class Tempo extends TimerTask
		{
			public void run()
			{
				bCome1 = false;
				Princ1 = Pacs;
			}
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			bPac1=cPac1 = dPac1= ePac1 = true;
			for (int linha = 0; linha <= 8; linha++)
			{
				for (int coluna = 0; coluna <= 14; coluna++)
				{
					if (mapa[linha][coluna] == 1)
					{
						g.setColor(Color.BLUE);
						g.fillRect(coluna*tam, linha*tam, tam, tam);
						
						if( (iXPac1/tam+1) == coluna && (iYPac1/tam) == linha)    dPac1 = false;
						if( (iXPac1/tam-1) == coluna && (iYPac1/tam) == linha && (( -(coluna+1)*tam + iXPac1) <=8 ))    ePac1 = false;
						if( (iXPac1/tam) == coluna && (iYPac1/tam-1) == linha && ((iYPac1 - (linha+1)*tam) <=9))    cPac1 = false;
						if( ((iXPac1+30)/tam) == coluna && (iYPac1/tam+1) == linha ) bPac1 = false;
					
					}
					if (mapa[linha][coluna] == 0)
					{
						g.setColor(Color.BLACK);
						g.fillRect(coluna*tam, linha*tam, tam, tam);
					}
					if (mapa[linha][coluna] == 2)
					{			
						if ((iXPac1 == coluna*tam)&&(iYPac1 == linha*tam))
						{
							bCome1 = true;
							Princ1 = Come;
							mapa[linha][coluna] = 0;
							timer = new Timer();
							timer.schedule(new Tempo(),15*1000);
						}
						g.drawImage(Cereja, coluna*tam, linha*tam, this);
					}
				}
			}
			g.drawImage(Princ1[iPac1], iXPac1, iYPac1, this);
			g.drawImage(Princ2[iPac2], iXPac2, iYPac2, this);
			if ((iXPac1==iXPac2)&&(iYPac1==iYPac2))
			{
				try
				{
					if ((bCome1)&&(!bCome2))
					{
						JOptionPane.showMessageDialog(null, "Parabens, voce ganhou o jogo!","Campeao",JOptionPane.INFORMATION_MESSAGE);
						System.exit(0); 
					}
					else
					{
						if ((bCome2)&&(!bCome1))
						{
							JOptionPane.showMessageDialog(null, "Te comeram... Voce morreu!","Game Over",JOptionPane.ERROR_MESSAGE);
							System.exit(0); 
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Um comeu o outro e outro comeu o um. Todos morreram!","Game Over",JOptionPane.ERROR_MESSAGE);
							System.exit(0); 
						}
					}
				} catch (Exception e) {System.exit(0);}
			}
		}
		
		public void run()
		{
			addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					switch (e.getKeyCode())
					{
						case KeyEvent.VK_RIGHT:
							if (((iXPac1+tam) > tam*15)|| !dPac1)
								return;
							iXPac1 += iPasso;
							switch (iPac1)
							{
								case PAC1:
									iPac1 = PAC2D;
									break;
								case PAC2D:
								case PAC2S:
								case PAC2A:
								case PAC2W:
									if (bVai)
									{
										iPac1 = PAC3D;
										bVai = false;
									}
									else
									{
										iPac1 = PAC1;
										bVai = true;
									}
									break;
								case PAC3D:
								case PAC3S:
								case PAC3W:
								case PAC3A:
									iPac1 = PAC2D;
									break;
							}
							break;
						case KeyEvent.VK_DOWN:
							if ((iYPac1+tam) > tam*9 || !bPac1)
								return;
							iYPac1 += iPasso;
							switch (iPac1)
							{
								case PAC1:
									iPac1 = PAC2S;
									break;
								case PAC2D:
								case PAC2S:
								case PAC2A:
								case PAC2W:
									if (bVai)
									{
										iPac1 = PAC3S;
										bVai = false;
									}
									else
									{
										iPac1 = PAC1;
										bVai = true;
									}
									break;
								case PAC3D:
								case PAC3S:
								case PAC3A:
								case PAC3W:
									iPac1 = PAC2S;
									break;
							}
							break;
						case KeyEvent.VK_LEFT:
							if (iXPac1 < 5 || !ePac1)
								return;
							iXPac1 -= iPasso;
							switch (iPac1)
							{
								case PAC1:
									iPac1 = PAC2A;
									break;
								case PAC2D:
								case PAC2S:
								case PAC2A:
								case PAC2W:
									if (bVai)
									{
										iPac1 = PAC3A;
										bVai = false;
									}
									else
									{
										iPac1 = PAC1;
										bVai = true;
									}
									break;
								case PAC3D:
								case PAC3S:
								case PAC3A:
								case PAC3W:
									iPac1 = PAC2A;
									break;
							}
							break;
						case KeyEvent.VK_UP:
							if (iYPac1 < 5 || !cPac1)
								return;
							iYPac1 -= iPasso;
							switch (iPac1)
							{
								case PAC1:
									iPac1 = PAC2W;
									break;
								case PAC2D:
								case PAC2S:
								case PAC2A:
								case PAC2W:
									if (bVai)
									{
										iPac1 = PAC3W;
										bVai = false;
									}
									else
									{
										iPac1 = PAC1;
										bVai = true;
									}
									break;
								case PAC3D:
								case PAC3S:
								case PAC3A:
								case PAC3W:
									iPac1 = PAC2W;
									break;
							}
							break;
					}
					repaint();
				}
				
				public void keyReleased(KeyEvent e) //Garantir que o Pacman sempre pare com a boca meio aberta
				{
					switch(iPac1)
					{
						case PAC1:
							if (e.getKeyCode() == KeyEvent.VK_RIGHT)
								iPac1 = PAC2D;
							if (e.getKeyCode() == KeyEvent.VK_DOWN)
								iPac1 = PAC2S;
							if (e.getKeyCode() == KeyEvent.VK_LEFT)
								iPac1 = PAC2A;
							if (e.getKeyCode() == KeyEvent.VK_UP)
								iPac1 = PAC2W;
							break;
						case PAC3D: iPac1 = PAC2D; break;
						case PAC3S: iPac1 = PAC2S; break;
						case PAC3A: iPac1 = PAC2A; break;
						case PAC3W: iPac1 = PAC2W; break;
					}
					repaint();
				}
			});
		}
	}
	
	class Fruta
	{
		
	}

	Client()
	{
		super("Pacman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setResizable(false);
		add(new Pacman());
		pack();
		setVisible(true);
	}
	
	public Dimension getPreferredSize() 
	{
		return new Dimension(981, 614);
	}
	
	public static void main(String[] args) 
	{		
		try 
		{
			Socket socket = new Socket("127.0.0.1", PORT);
			is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			os = new PrintStream(new BufferedOutputStream(socket.getOutputStream(), 1024), true);
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Servidor desconhecido.");
		} catch (IOException e) 
		{
			System.err.println("Impossivel acessar servidor.");
		}
		System.out.println("Conectei ao servidor.");
		
		try
		{
			id_client = Integer.parseInt(is.readLine());
		} catch(Exception e) {}
		System.out.println("Sou o cliente " + id_client);
		
		new Thread(new Client()).start();
	}
	
	public void run()
	{
		String come;
		if (id_client == 0)
		{
			iPac1 = 1;
			iXPac1 = 0;
			iYPac1 = 0;
			iPac2 = 3;
			iXPac2 = 910;
			iYPac2 = 520;
		}
		else
		{
			iPac2 = 1;
			iXPac2 = 0;
			iYPac2 = 0;
			iPac1 = 3;
			iXPac1 = 910;
			iYPac1 = 520;
		}
		repaint();
		do 
		{
			if (id_client == 0)
			{
				os.println(iPac1);
				os.println(iXPac1);
				os.println(iYPac1);
				os.println(bCome1);
				os.flush();
				try
				{
					iPac2 = Integer.parseInt(is.readLine());
					iXPac2 = Integer.parseInt(is.readLine());
					iYPac2 = Integer.parseInt(is.readLine());
					come = is.readLine();
					if (come.equals("true"))
					{
						bCome2 = true;
						Princ2 = Come;
					}
					else
					{
						bCome2 = false;
						Princ2 = Pacs;
					}
				} catch (IOException e) {}
				repaint();
			}
			else
			{
				try
				{
					iPac2 = Integer.parseInt(is.readLine());
					iXPac2 = Integer.parseInt(is.readLine());
					iYPac2 = Integer.parseInt(is.readLine());
					come = is.readLine();
					if (come.equals("true"))
					{
						bCome2 = true;
						Princ2 = Come;
					}
					else
					{
						bCome2 = false;
						Princ2 = Pacs;
					}
				} catch (IOException e) {}
				repaint();
				os.println(iPac1);
				os.println(iXPac1);
				os.println(iYPac1);
				os.println(bCome1);
				os.flush();
			}
		} while (true);
	}
}
