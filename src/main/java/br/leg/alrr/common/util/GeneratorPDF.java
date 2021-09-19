/**
 * 
 */
package br.leg.alrr.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Classe responsável pela criação dos arquivos pdf
 * 
 * @author rafaell
 *
 */
public class GeneratorPDF {
	
	private ByteArrayOutputStream baos;
    private HttpServletResponse response;
    private FacesContext context;
    private String pathLogoALE;
    private String pathLogoSIC;
    
    public GeneratorPDF() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }
    
    
//    public void solitacaoPDF(Solicitacao solicitacao) throws IOException {
//    	
//    	// criação do objeto documento
//        Document document = new Document(PageSize.A4, 72, 72, 30, 72);
//        baos = new ByteArrayOutputStream();
//
//        try {
//        	
//            Locale local = new Locale("pt", "BR");
//            //SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", local);
//            
//
//            pathLogoALE = context.getExternalContext().getRealPath("/pdf/imagens/logo-ale.png");
//            pathLogoSIC = context.getExternalContext().getRealPath("/pdf/imagens/logo-sic.png");
//            PdfWriter writer = PdfWriter.getInstance(document, baos);
//            
//            document.open();
//            
//            PdfContentByte cb = writer.getDirectContent();
//
//            
//            
//            Image logoAle = Image.getInstance(pathLogoALE);
//            Image logoSic = Image.getInstance(pathLogoSIC);
//
//            logoAle.scaleAbsolute(59, 49);
//            logoAle.setAbsolutePosition(70, 770);
//
//            logoSic.scaleAbsolute(59, 49);
//            logoSic.setAbsolutePosition(470, 770);
//
//            Font fonteNegrito = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
//            Font fonteItalico = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC);
//            Font fonteNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
//            
//            
//            // adicionando um parágrafo ao documento
//            Paragraph p = new Paragraph("ASSEMBLEIA LEGISLATIVA DO ESTADO DE RORAIMA");
//            Paragraph p1 = new Paragraph("Amazônia: Patrimônio dos Brasileiros", fonteItalico);
//            Paragraph p2 = new Paragraph("Serviço de Informações ao Cidadão", fonteNegrito);
//            Paragraph p3 = new Paragraph("Comprovante - SIC", fonteNegrito);
//            Paragraph p10 = new Paragraph("Assunto: " + solicitacao.getAssunto().getDescricao(), fonteNegrito);
//            Paragraph p4 = new Paragraph("Protocolo: " + solicitacao.getId(), fonteNormal);
//            Paragraph p5 = new Paragraph("Abertura: " + solicitacao.getDataSolicitacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", local)), fonteNormal);
//            Paragraph p6 = new Paragraph("Solicitante: " + solicitacao.getSolicitante().getNome(), fonteNormal);
//            Paragraph p7 = new Paragraph("E-Mail: " + solicitacao.getSolicitante().getEmail(), fonteNormal);
//            Paragraph p8 = new Paragraph("Telefone: " + solicitacao.getSolicitante().getTelefone(), fonteNormal);
//            Paragraph p11 = new Paragraph("Solicitacão:", fonteNegrito);
//            
//            String conteudo = solicitacao.getConteudo().replace("<p>", "");
//            conteudo = conteudo.replace("</p>", "");
//            Paragraph p9 = new Paragraph(conteudo, fonteNormal);
//            
//            //Retangulo (Largura,descer, tamanho, subir, curva )
//
//            cb.roundRectangle(65f, 250f, 465f, 270f, 20f);
//            
//            cb.stroke();
//
//
//
//            // adicionando espaço antes ao paragrafo
//            p3.setSpacingBefore(40);
//            p10.setSpacingBefore(30);
//            p4.setSpacingBefore(2);
//            //p4.setSpacingAfter(2);
//            p5.setSpacingBefore(2);
//            p8.setSpacingBefore(2);
//            p8.setSpacingAfter(4);
//            p11.setSpacingBefore(2);
//            p9.setSpacingBefore(20);
//            
//            //adicionando espaço entre linhas no paragrafo
//            //p4.setLeading(25);
//            // adicionando recuo na primeira linha
//            //p4.setFirstLineIndent(30);;
//
//            // alinhamento do texto
//            p.setAlignment(Element.ALIGN_CENTER);
//            p1.setAlignment(Element.ALIGN_CENTER);
//            p2.setAlignment(Element.ALIGN_CENTER);
//            p3.setAlignment(Element.ALIGN_CENTER);
//            p4.setAlignment(Element.ALIGN_LEFT);
//            p5.setAlignment(Element.ALIGN_LEFT);
//            p6.setAlignment(Element.ALIGN_LEFT);
//            p7.setAlignment(Element.ALIGN_LEFT);
//            p8.setAlignment(Element.ALIGN_LEFT);
//            p9.setAlignment(Element.ALIGN_JUSTIFIED);
//            p10.setAlignment(Element.ALIGN_LEFT);
//            p11.setAlignment(Element.ALIGN_LEFT);
//
//            document.add(p);
//            document.add(p1);
//            document.add(p2);
//            document.add(p3);
//            document.add(p10);
//            document.add(p4);
//            document.add(p5);
//            document.add(p6);
//            document.add(p7);
//            document.add(p8);
//            document.add(p11);
//            document.add(p9);
//            
//
//            document.add(logoAle);
//            document.add(logoSic);
//            
//
//            document.addSubject("Solicitação");
//            document.addKeywords("al.rr.leg.br");
//            document.addCreator("ALE-RR");
//            document.addAuthor("ALE-RR");
//
//            document.close();
//
//            response.reset();
//            response.setContentType("application/pdf");
//            response.setContentLength(baos.size());
//            response.setHeader("Content-disposition", "attachment; filename=solicitacao.pdf");
//            response.getOutputStream().write(baos.toByteArray());
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//
//            context.responseComplete();
//
//        } catch (DocumentException e) {
//            throw new IOException(e.getMessage());
//        }
//    }
}
