package sistema_tcc.web;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import sistema_tcc.dominio.Tcc;
import sistema_tcc.dominio.Usuario;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AtaGenerator {

    public static byte[] gerarAtaPdf(Tcc tcc) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("ATA DE DEFESA DE TCC", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n\n"));

            // Corpo
            Font fontCorpo = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // CORREÇÃO: Tratamento robusto da data (String ou LocalDate)
            String dataFormatada = "[DATA INDEFINIDA]";
            if (tcc.getDataBanca() != null && !tcc.getDataBanca().isEmpty()) {
                try {
                    // Tenta parsear caso venha como ISO (yyyy-MM-dd) e formatar para BR
                    // Se já vier como dd/MM/yyyy, o parse pode falhar ou ser desnecessário
                    if (tcc.getDataBanca().contains("-")) {
                        dataFormatada = LocalDate.parse(tcc.getDataBanca()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    } else {
                        dataFormatada = tcc.getDataBanca(); // Assume que já está formatada
                    }
                } catch (Exception e) {
                    dataFormatada = tcc.getDataBanca(); // Fallback: usa como está
                }
            }

            String texto = String.format(
                    "Aos %s, reuniu-se a banca examinadora para avaliar o Trabalho de Conclusão de Curso intitulado:\n\n" +
                            "\"%s\"\n\n" +
                            "De autoria do aluno(a): %s\n" +
                            "Sob orientação do professor(a): %s\n\n" +
                            "A banca foi composta pelos seguintes membros:\n",
                    dataFormatada,
                    tcc.getTitulo(),
                    tcc.getAutor().getNome(),
                    (tcc.getOrientador() != null ? tcc.getOrientador().getNome() : "N/A")
            );

            Paragraph paragrafo1 = new Paragraph(texto, fontCorpo);
            paragrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragrafo1);

            // ... (restante do código igual) ...

            // Lista de Membros
            com.lowagie.text.List lista = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED);
            if (tcc.getBancaMembros() != null) {
                for (Usuario membro : tcc.getBancaMembros()) {
                    lista.add(new ListItem(membro.getNome()));
                }
            }
            document.add(lista);
            document.add(new Paragraph("\n"));

            // Resultado
            String status = (tcc.getNotaFinal() != null && tcc.getNotaFinal() >= 6.0) ? "APROVADO" : "REPROVADO";

            String resultado = String.format(
                    "Após a apresentação, a banca atribuiu a nota final %.2f.\n" +
                            "Situação: %s.\n\n" +
                            "Parecer Final:\n%s",
                    (tcc.getNotaFinal() != null ? tcc.getNotaFinal() : 0.0),
                    status,
                    (tcc.getParecerFinal() != null ? tcc.getParecerFinal() : "Sem parecer.")
            );

            document.add(new Paragraph(resultado, fontCorpo));

            // Assinatura
            document.add(new Paragraph("\n\n\n\n____________________________________"));
            Paragraph assinatura = new Paragraph("Assinatura do Orientador", fontCorpo);
            document.add(assinatura);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}