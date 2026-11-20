package mpkm;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.stereotype.Service;

@Service
public class LaporanService {

    private final DataSource dataSource;

    public LaporanService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void tampilkanLaporan() throws JRException, SQLException, IOException {
        try (InputStream jrxml = LaporanService.class.getResourceAsStream("/reports/LaporanKeuangan.jrxml");
             Connection connection = dataSource.getConnection()) {
            if (jrxml == null) {
                throw new JRException("File laporan LaporanKeuangan.jrxml tidak ditemukan.");
            }
            JasperReport report = JasperCompileManager.compileReport(jrxml);
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), connection);
            JasperViewer.viewReport(print, false);
        }
    }
}
