/*
 * AUTORES:
 * Jose Gonzalez - A01036121
 * Adrian Martinez - A01280252
 * 
 * Proxy HTTP simple
 * 
 * 25/11/15
 * 
 * */
import java.net.*;
import java.io.*;

public class Proxy {
	public static void main(String[] args) throws IOException {

		Socket socketCliente = null;
		ServerSocket socketServidor = null;

		try {
			socketServidor = new ServerSocket(80);
			socketCliente = socketServidor.accept();
		} catch (IOException e) {
			System.out.println(e);
		}

		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(
					socketCliente.getInputStream()));

			String peticion;
			int iterador = 0;
			String urlDestino = "";
			while ((peticion = in.readLine()) != null) {
				try {

					String[] str = peticion.split(" ");
					String str2 = str[iterador];

					if (iterador == 0 && !str2.equals("GET")) {
						System.out.println("Not implemented: error 501");
						return;
					}

				} catch (Exception e) {
					break;
				}

				if (iterador == 0) {
					String[] fragmentosHTML = peticion.split(" ");
					urlDestino = fragmentosHTML[1];
				}
				iterador++;
			}

			InputStream is = null;
			try {

				URL url = new URL(urlDestino);
				URLConnection conexion = url.openConnection();

				try {
					is = conexion.getInputStream();

				} catch (IOException e) {
					System.out.println("Bad request: error 400");
					return;
				}

				InputStream entrada = is;
				OutputStream salida = socketCliente.getOutputStream();
				byte[] reply = new byte[4096];

				int cantActual;
				try {
					while ((cantActual = entrada.read(reply)) != -1) {
						salida.write(reply, 0, cantActual);
					}
				} catch (IOException e) {
					System.out.println(e);
				}

				salida.close();
				entrada.close();
			} catch (Exception e) {
				System.out.println(e);
			}

			is.close();
			in.close();
			socketServidor.close();
			socketCliente.close();

		} catch (IOException e) {
			System.out.println(e);
			;
		}
	}

}
