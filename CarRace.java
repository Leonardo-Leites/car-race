import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CarRace extends JPanel implements Runnable, KeyListener {

	static Random gerador = new Random();
	static DisplayMode monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();

	Font Fontetest = new Font("Arial", Font.BOLD, 15);

	// pista
	int velocidadePista;

	// carro OBS
	int sortcarro = gerador.nextInt(5);

	// dificuldade
	int dif = 0;

	// JOGADOR
	boolean praDir = false, praEs = false, praCim = false, praBaix = false;
	int playerPosX = 140, playerPosY = 250;
	int playerTamX = 250, playerTamY = 150;
	int playerVelocidade = 5;
	int playerVidaInicial = 5;
	int playerVidas = playerVidaInicial;

	// OBST�SCULOS
	int ObsposY = -200; // posicao dos Obst�culos
	int velocidadeObs = 5;
	int direcaoPosX = 1, direcaoPosY = 1;
	int tamObsX = 80, tamObsY = 115;
	int vetorc1[] = { 150, 300, 210, 355, 140, 300, 190, 310, 150, 240 };// provis�rio, a ideia � armazenar as posi��es
																			// em um array para
	int vetorc2[] = { 150, 100, 355, 355, 300, 300, 280, 70, 280, 240 };// melhor controle e espa�o
	int car1 = 0;
	int car2 = 0;

	// controle velocidade/ vezes que os carros passam na tela

	int vezes = 0;
	int vezesaux = 10;

	// pause
	boolean pause = true;

	// "criando" as Imagens
	BufferedImage imagem;
	BufferedImage imagemPista;
	BufferedImage imagemEU;// xEs = 285 e xDi = - 10
	BufferedImage imagemOBS;// xEs = 70 e Xdir = 355

	public static void main(String[] args) throws Exception {

		// Criando o JFrame/ Janela
		JFrame janela = new JFrame("CarDrive");// digo o nome da janela
		janela.setSize(500, 600);// digo o tamanho da jenal
		janela.setVisible(true);// digo que ela ficara visivel na tela
		janela.setLocationRelativeTo(null);// digo que ela tem que aparecer no meio da tela
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// digo que caso clicar para fechar a janela o jogo para
		janela.setResizable(false);// digo que ela n�o pode ser redimencionada
		janela.setLayout(null);

		// crianda minha pista/ movimenta��o
		CarRace pista = new CarRace();
		pista.setBounds(0, 100, monitor.getWidth(), monitor.getHeight());// estou pegando o tamanho da minha pista

		// criando o JPanel/ Local onde eu desenho
		CarRace desenha = new CarRace();
		desenha.setSize(500, 600);// digo onde ele pode desenhar
		desenha.setVisible(true);// digo que o meu desenho ficara visivel na minha janela

		janela.add(desenha);// adiciono meu desenha na janela
		janela.addKeyListener(desenha);// digo que vou ler uma informa��o e esta deve ser adicionada no meu desenha
		janela.addKeyListener(pista);// digo que vou ler uma informa��o e esta deve ser adicionada na minha pista

	}

	public CarRace() throws Exception {// criando o projeto principal

		Thread processoDoJogo = new Thread(this);
		processoDoJogo.start();

		try {// tenta achar a imagem
			imagem = ImageIO.read(new File("coracao.png"));
		} catch (IOException e) {// n�o achou a imagem
			System.out.println("N�o achei a imagem do cora��o");
			e.printStackTrace();
		}

		try {
			imagemPista = ImageIO.read(new File("pista.png"));
		} catch (IOException e) {
			System.out.println("N�o achei a imagem da pista");
			e.printStackTrace();
		}

		velocidadePista = 0;// acha a imagem da pista e fala que a velocidade em y dela � = 0

		try {
			imagemEU = ImageIO.read(new File("EU.png"));
		} catch (IOException e) {
			System.out.println("N�o achei a imagem da pista");
			e.printStackTrace();
		}

		try {
			if (sortcarro == 0 || sortcarro == 5) {// acha o n�mero do Obs e verifica se ele existe ou n�o
				sortcarro = 1;
			}
			imagemOBS = ImageIO.read(new File("carro" + sortcarro + ".png"));// se existir ele pega a imagem na posi��o
																				// sorteada
		} catch (IOException e) {
			System.out.println("N�o achei a imagem da pista");
			e.printStackTrace();
		}
	}

	public void run() {// processo que faz o jogo ficar atualizando/ pintando/dormindo

		while (true) {

			if (!pause) {
				atualiza(); // atualiza
				repaint(); // pinta
			}

			sono(); // dorme
		}

	}

	public void atualiza() {// onde fica as atualiza��es do jogo como as posi��es

		System.out.println("Player posy " + playerPosY + "\nPlayer posx " +
				playerPosX + "\nOBS posy " + ObsposY + "\nObs posXc1 " + vetorc1[car1] + "\nObs posXc2 "
				+ vetorc2[car2]);

		if (playerVelocidade <= 10) {

			ObsposY += velocidadeObs;

			if (playerPosX < vetorc1[car1] - 80 && playerPosX < vetorc2[car2] - 80 // colis�o n�o terminada e
																					// provavelmente errada
					|| playerPosX < vetorc1[car1] && playerPosX < vetorc2[car2]) {// trava no lado esquerdo

				if (playerPosY + 150 > ObsposY && playerPosY < ObsposY + 115) {// comentar para melhor desempenho do
																				// game
					// pause = true;
					playerVidas--;
					playerPosX = 250;
					playerPosY = 430;
					ObsposY = -200;
				}
			}

			// verificando quantas vidas ha

			if (playerVidas == 0) {

				int resposta = JOptionPane.showConfirmDialog(this, "Voc� � muito ruim!!! Quer praticar mais?");
				if (resposta == JOptionPane.OK_OPTION) {
					pause = true;
					playerVidas = playerVidaInicial;
					playerVelocidade = 5;
					playerVelocidade = 4;
					velocidadeObs = 5;
					praEs = false;
					praDir = false;
					praCim = false;
					praBaix = false;
					vezes = 0;
					dif = 0;
					car1 = 0;
					car2 = 0;
					playerPosY = 0;
					repaint();

				} else {
					System.exit(0);
				}
			}

			// posi��o dos Obs em Y + vezes + velocidade

			if (vezes > vezesaux) {

				dif += 1;
				vezesaux += 10;
				velocidadeObs += 1;
				playerVelocidade += 1;
			}

			if (ObsposY > (600 + 45) || ObsposY < -200) {

				if (vetorc2[car2] != vetorc1[car1]) {
					vezes++;
				}

				ObsposY = -100;
				vezes++;
				car1++;
				car2++;

				if (car1 == 10) {
					car1 = 0;
				}
				if (car2 == 10) {
					car2 = 0;
				}
			}

			// player

			// atualiza��o da velocidade do player e da pista

			if (praBaix == true) {
				playerVelocidade = 4;
			} else if (praBaix == false) {
				playerVelocidade = 5;
			}

			// dire��o do player

			if (praEs && playerPosX + 10 > 0) {
				playerPosX -= playerVelocidade;
			}
			if (praDir && playerPosX + 218 < 500) {
				playerPosX += playerVelocidade;
			}
			if (praBaix && playerPosY + 165 < 600) {
				playerPosY += playerVelocidade;
			}
			if (praCim && playerPosY + 2 > 0) {
				playerPosY -= playerVelocidade;
			}
		} else {
			velocidadeObs = 10;
			playerVelocidade = 10;
		}
	}

	// dormir
	public void sono() {
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2); // limpa a tela

		Graphics2D g = (Graphics2D) g2.create();// cria o 2D

		// estrada

		int altura = CarRace.monitor.getHeight();// parte pega da live

		g.drawImage(imagemPista, 0, velocidadePista - altura, imagemPista.getWidth(), imagemPista.getHeight(), null);
		g.drawImage(imagemPista, 0, velocidadePista, imagemPista.getWidth(), -imagemPista.getHeight(), null);
		g.drawImage(imagemPista, 0, velocidadePista, imagemPista.getWidth(), imagemPista.getHeight(), null);

		velocidadePista += 12;

		if (velocidadePista >= altura)
			velocidadePista = 0;

		// player

		g.drawImage(imagemEU, playerPosX, playerPosY, playerTamX, playerTamY, null);

		// obstaculos

		g.drawImage(imagemOBS, vetorc1[car1], ObsposY, tamObsX, tamObsY, null);
		g.drawImage(imagemOBS, vetorc2[car2], ObsposY, tamObsX, tamObsY, null);

		// exibindo o numero de vezes

		g.setColor(Color.BLACK);
		g.setFont(Fontetest);
		g.drawString("Voc� passou " + vezes, 370, 20);

		// vidas

		for (int i = 0; i < playerVidas; i++) {
			g.drawImage(imagem, 30 + (i * 20), 10, 20, 20, null);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {// verifica a tecla precionada

		if (e.getKeyCode() == KeyEvent.VK_D) {
			praDir = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			praEs = true;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			praBaix = true;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			praCim = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			pause = false;
		}
		if (e.getKeyChar() == '-') {// para desenvolvimento
			playerVidas--;
		}
		if (e.getKeyCode() == '+') {// para desenvolvimento
			velocidadeObs++;
			playerVelocidade++;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {// verifica se a tecla foi solta

		if (e.getKeyCode() == KeyEvent.VK_D) {
			praDir = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			praEs = false;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			praCim = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			praBaix = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}