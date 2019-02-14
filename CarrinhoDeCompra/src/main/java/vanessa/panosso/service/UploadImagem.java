package vanessa.panosso.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContext;


public class UploadImagem {
	private ServletContext context;

	private DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public String inserImagemDiretorio(FileItem item) throws Exception{

    	final String PATH_ARQUIVOS = context.getRealPath("/img");

        final String DIR_DATA_ATUAL = format.format(Calendar.getInstance().getTime());
        final String PATH_ABSOLUTO  = (PATH_ARQUIVOS + DIR_DATA_ATUAL );
        System.err.println(PATH_ABSOLUTO);
        try {


            File diretorio  = new File(PATH_ABSOLUTO);

            if(!diretorio.exists()) {
            	 diretorio.mkdir();
            }
               

            String fileName = item.getName();
            System.out.println(fileName);
            String arq[] = fileName.split("\\\\");
            System.out.println(arq);

            for (int i = 0; i < arq.length; i++) {
                fileName = arq[i];
            }

            File file = new File(diretorio,fileName);
            FileOutputStream out = new FileOutputStream(file);
            System.err.println("out");

            out.write(item.getFoto());
            System.err.println("write");

            out.flush();
            out.close();


            // NO FINAL ELE TE RETORNA O CAMINHO DA PASTA ONDE VC SALVOU A IMAGEN
            // ESSA STRING VC PODE ARMAZENAR NA TABELA DO PRODUTO
            // NO CAMPO : CAMINHO_FOTO
            return "/" + DIR_DATA_ATUAL + "/" +item.getName();

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Erro ao carregar imagem para o diretorio !!\n "
                    + "Error : "      + e.getMessage() 
                    + "\nCausa : " + e.getCause());
        }

    }
    
    public UploadImagem(ServletContext context) {
		this.context = context;
	}
}
