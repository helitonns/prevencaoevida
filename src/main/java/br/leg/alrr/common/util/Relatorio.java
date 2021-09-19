package br.leg.alrr.common.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ibm.icu.text.DecimalFormat;

//import br.leg.alrr.portaldatransparencia.model.FolhaDePagamento;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Relatorio<T> {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private InputStream subreport;
    private InputStream logo2;

    public Relatorio() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

//    public void exportarTxt(List<FolhaDePagamento> lista) throws IOException {
//
//        baos = new ByteArrayOutputStream();
//        // Prepara para escrever no arquivo
//        File arquivoTxt = new File(context.getExternalContext().getRealPath("/arquivos/consultaRemuneracao.txt"));
//        //System.out.print(arquivoTxt);
//        FileWriter fileWriter = new FileWriter(arquivoTxt);
//        BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
//
//        // Escreve e fecha arquivo
//        for (FolhaDePagamento dados : lista) {
//            bufferWriter.write(dados.getPeriodoReferencia() + ";");
//            bufferWriter.write(dados.getServidor().getNome() + ";");
//            bufferWriter.write(dados.getServidor().getCargo() + ";");
//            bufferWriter.write(dados.getServidor().getSetor() + ";");
//            bufferWriter.write(dados.getServidor().getOrgao() + ";");
//            bufferWriter.write(dados.getServidor().getCentroDeCusto() + ";");
//            bufferWriter.write(dados.getServidor().getVinculo() + ";");
//            bufferWriter.write(dados.getTipoFolhaPagamento().getDescricao() + ";");
//            bufferWriter.write(dados.getPagamento().getTotalProventos() + ";");
//            bufferWriter.write(dados.getPagamento().getTotalDescontos() + ";");
//            bufferWriter.write(dados.getPagamento().getTotalLiquido() + " ");
//            bufferWriter.write("\n");
//
//        }
//
//        bufferWriter.flush();
//        bufferWriter.close();
//
//        File arquivo = new File(context.getExternalContext().getRealPath("/arquivos/consultaRemuneracao.txt"));
//        int tamanho = (int) arquivo.length();
//
//        response.reset();
//        response.setContentType("text/plain");
//        response.setContentLength(tamanho);
//        response.setHeader("Content-disposition", "attachment; filename=consultaRemuneracao.txt");
//
//        OutputStream output = response.getOutputStream();
//        Files.copy(arquivo.toPath(), output); // escreve bytes no f
//
//        context.responseComplete();
//
//    }

//    public void exportarOds(List<FolhaDePagamento> lista) throws IOException {
//
//        //String fileName = "NewOdsFile.xls";
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Consulta Remuneração");
//        baos = new ByteArrayOutputStream();
//
//        
//        // Configurando Header
//        
//        HSSFRow rowhead = sheet.createRow((short)0);
//        rowhead.createCell(0).setCellValue("Referência");
//        rowhead.createCell(1).setCellValue("Nome");
//        rowhead.createCell(2).setCellValue("Cargo");
//        rowhead.createCell(3).setCellValue("Setor");
//        rowhead.createCell(4).setCellValue("Órgão");
//        rowhead.createCell(5).setCellValue("C. de custo");
//        rowhead.createCell(6).setCellValue("Vínculo");
//        rowhead.createCell(7).setCellValue("Tipo folha");
//        rowhead.createCell(8).setCellValue("Proventos");
//        rowhead.createCell(9).setCellValue("Descontos");
//        rowhead.createCell(10).setCellValue("Líquido");
//        
//        // Adicionando os dados dos produtos na planilha
//        
//        int rownum = 1;
//       
//        for (FolhaDePagamento dados : lista) {
//        	HSSFRow row = sheet.createRow((short)rownum++);
//        	DecimalFormat numeroFormatado = new DecimalFormat("#,##0.00");
//        	
//        	row.createCell(0).setCellValue(dados.getPeriodoReferencia());
//            row.createCell(1).setCellValue(dados.getServidor().getNome());
//            row.createCell(2).setCellValue(dados.getServidor().getCargo());
//            row.createCell(3).setCellValue(dados.getServidor().getSetor());
//            row.createCell(4).setCellValue(dados.getServidor().getOrgao());
//            row.createCell(5).setCellValue(dados.getServidor().getCentroDeCusto());
//            row.createCell(6).setCellValue(dados.getServidor().getVinculo());
//            row.createCell(7).setCellValue(dados.getTipoFolhaPagamento().getDescricao());
//            row.createCell(8).setCellValue(numeroFormatado.format(dados.getPagamento().getTotalProventos()));
//            row.createCell(9).setCellValue(numeroFormatado.format(dados.getPagamento().getTotalDescontos()));
//            row.createCell(10).setCellValue(numeroFormatado.format(dados.getPagamento().getTotalLiquido()));
//            
//            
//        }
//        
//      
//
//        try {
//        	
//
//            response.reset();
//            response.setContentType("application/vnd.oasis.opendocument.spreadsheet");
//            response.setHeader("Content-disposition", "attachment; filename=consultaRemuneracao.ods");
//            response.getOutputStream().write(baos.toByteArray());
//
//            OutputStream out = response.getOutputStream();
//            workbook.write(out);
//            out.flush();
//            out.close();
//
//            context.responseComplete();
//
//            //FileOutputStream out
//              //     = new FileOutputStream(new File(fileName));
//            //workbook.write(out);
//            //out.close();
//            //workbook.close();
//            System.out.println("Arquivo criado com sucesso!");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            System.out.println("Arquivo não encontrado!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Erro na edição do arquivo!");
//        }
//    }

    public void exportarPdf(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/pdf/jasper/relatorio.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/pdf/jasper/cabecalho.jasper");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/pdf/imagens/logo-ale.png");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-ale", logo2);

        baos = new ByteArrayOutputStream();

        try {
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
	       	JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
	       	params.put("cabecalho", dsc);
        	

            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=RelatorioDePresenca.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar relatório!"));
        }
    }

}
