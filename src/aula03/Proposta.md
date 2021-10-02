# Descri��o do comportamento observado

O c�digo base n�o constrola o comportamento das threads se interrelacionam e aplica lock no la�o. 
Os seguintes comportamentos foram observados:
1) Quando o Consumer � iniciado primeiro, o la�o for � consumido totalmente com valor igual a zero para apenas ent�o liberar o lock para o Producer. O Producer, ent�o, adiciona todos os demais valores.
2) Quando o Producer � iniciado primeiro, s�o adicionados todos os valores primeiro para que sejam consumidos apenas posteriormente.
3) Situa��es de concorr�ncia de acesso � vari�vel tamb�m ocorreram, n�o sendo poss�vel prever o comportamento de soma e subtra��o.
	
# Problemas identificados:
O comportamento adotado pela solu��o � a estrutura pilha, o tempo tomado para processar itens mais velhos � maior que itens mais novos pela situa��o �nica de ordena��o de acesso. Nesse caso, o comportamento de fila encadeada resolveria a situa��o de consumo de itens mais novos antes de itens mais velhos, mas aumentaria o tempo de processamento.
Outro problema identificado � que a complexidade da solu��o � aumentada, uma vez que ser� necess�rio gerir mem�ria e ser� empregada uma estrutura de dados que n�o existe atualmente na implementa��o.

Assim, outra solu��o que utiliza o conceito de fila encadeada � a utiliza��o e Object#notify e Object#wait nos elementos, Assim, embora o tempo e processamento seja potenciamente maior, nenhum dado novo � consumido ou gerado antes que o anterior tenha sido processado. 
  
Refer�ncia:
```java
public class Main {
	public volatile static int produtos = 0;
	
	public static void main(String[] args) {
		Produtor p1 = new Produtor(1);
		Consumidor c1 = new Consumidor(2);
		p1.start();
		c1.start();
	}
}

class Produtor extends Thread {
	int nProdutor = 0;
	private static volatile Object lock = new Object();
	Produtor(int num){
		this.nProdutor = num;
	}
	public void run() {
		for(int i = 0; i <= 20; i++) {
			synchronized( lock ) 
			{
				if (Main.produtos < 100)
					Main.produtos = Main.produtos + 1;
			}
			System.out.println("Produtor: " + nProdutor + ": " + Main.produtos);
		}
	}
}

class Consumidor extends Thread {
	int nConsumidor = 0;
	private static volatile Object lock = new Object();
	Consumidor(int num){
		this.nConsumidor = num;
	}
	public void run() {
		for(int i = 0; i <= 20; i++) {
			synchronized( lock ) 
			{	
				if ( Main.produtos > 0)
					Main.produtos = Main.produtos - 1;
			}
			System.out.println("Consumidor: " + nConsumidor + ": " + Main.produtos);
		}
	}
}
```
