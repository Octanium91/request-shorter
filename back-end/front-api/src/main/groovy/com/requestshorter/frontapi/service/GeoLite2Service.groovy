package com.requestshorter.frontapi.service

import com.maxmind.geoip2.DatabaseReader
import groovy.util.logging.Slf4j
import org.apache.tools.tar.TarEntry
import org.apache.tools.tar.TarInputStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.nio.file.Files
import java.util.zip.GZIPInputStream

@Slf4j
@Service
class GeoLite2Service {

    @Value('${maxmindLicKey}')
    private final String licKey = ""
    private String maxmindFolder = "./files/maxmind"
    DatabaseReader asnReader = null
    DatabaseReader cityReader = null

    @PostConstruct
    private void postInit() {
        String asnDBPath = getDBPathString("GeoLite2-ASN")
        if (asnDBPath) {
            asnReader = new DatabaseReader.Builder(new File(asnDBPath)).build()
        }
        String cityDBPath = getDBPathString("GeoLite2-City")
        if (cityDBPath) {
            cityReader = new DatabaseReader.Builder(new File(cityDBPath)).build()
        }
    }

    private static void unTar(String tarFile, File dest) throws IOException {

        dest.mkdir();

        TarInputStream tin = new TarInputStream( new GZIPInputStream
                ( new FileInputStream(new File(tarFile))));

        String pathFolder = null

        TarEntry tarEntry = tin.getNextEntry();

        while (tarEntry != null){//create a file with the same name as the tarEntry

            File destPath = new File(dest.toString() + File.separatorChar + tarEntry.getName());

            if(tarEntry.isDirectory()){

                pathFolder = tarEntry.getName()
//                destPath.mkdir();

            } else {

                destPath = new File(dest.toString() + File.separatorChar + tarEntry.getName().replace(pathFolder, ""));

                FileOutputStream fout = new FileOutputStream(destPath);

                tin.copyEntryContents(fout);

                fout.close();
            }
            tarEntry = tin.getNextEntry();
        }
        tin.close();
    }

    String getSha256(String editionId) {
        try {
            new URL("https://download.maxmind.com/app/geoip_download?edition_id=${editionId}&license_key=${licKey}&suffix=tar.gz.sha256").getText().split()[0]
        } catch(e) {
            log.warn("Maxmind GeoLite2: failed to get SHA-256 of ${editionId}, '${"https://download.maxmind.com/app/geoip_download?edition_id=${editionId}&license_key=${licKey}&suffix=tar.gz.sha256"}'")
            null
        }
    }

    byte[] getFileTarGz(String editionId) {
        try {
            new URL("https://download.maxmind.com/app/geoip_download?edition_id=${editionId}&license_key=${licKey}&suffix=tar.gz").getBytes()
        } catch(e) {
            log.warn("Maxmind GeoLite2: failed to get database archive (tar.gz) of ${editionId}, '${"https://download.maxmind.com/app/geoip_download?edition_id=${editionId}&license_key=${licKey}&suffix=tar.gz"}'")
            null
        }
    }

    boolean sha256IsExist(String editionId, String sha256) {
        new File("${maxmindFolder}/geoip/${editionId}/${sha256}").exists()
    }

    void updateDb(String editionId, String sha256) {
        File dbFolder = new File("${maxmindFolder}/geoip/${editionId}/${sha256}")
        if (!dbFolder.exists()) {
            dbFolder.mkdirs()
        }
        byte[] file = getFileTarGz(editionId)
        File disFile = new File("${maxmindFolder}/geoip/${editionId}/${sha256}/dis.tar.gz")
        if (disFile.exists()) {
            disFile.delete()
        }
        Files.write(disFile.toPath(), file)
        String disFileSha256 = disFile.getBytes().digest('SHA-256')
        if (sha256 == disFileSha256) {
            unTar("${maxmindFolder}/geoip/${editionId}/${sha256}/dis.tar.gz", dbFolder)
            disFile.delete()
            File dbFile = new File("${maxmindFolder}/geoip/${editionId}/${sha256}/${editionId}.mmdb")
            if (dbFile.exists()) {
                if (editionId == "GeoLite2-ASN") {
                    asnReader = new DatabaseReader.Builder(dbFile).build()
                }
                if (editionId == "GeoLite2-City") {
                    cityReader = new DatabaseReader.Builder(dbFile).build()
                }
                try {
                    File dbsFolder = new File("${maxmindFolder}/geoip/${editionId}")
                    dbsFolder.listFiles().each {
                        if (it.getName() != sha256) {
                            it.deleteDir()
                        }
                    }
                } catch(e) {

                }
                log.info("Maxmind GeoLite2: Database ${editionId} is successfully updated!")
            } else {
                dbFolder.deleteDir()
                log.warn("Maxmind GeoLite2: Database file ${editionId}.mmdb (${sha256}) is not found!")
            }
        } else {
            disFile.delete()
            dbFolder.deleteDir()
            log.warn("Maxmind GeoLite2: Database ${editionId} (dis.tar.gz) SHA-256 Does not match! s: ${sha256}, f: ${disFileSha256}")
        }
    }

    String getDBPathString(String editionId) {
        try {
            String firstSha256 = new File("${maxmindFolder}/geoip/${editionId}").listFiles()[0].getName()
            File dbFile = new File("${maxmindFolder}/geoip/${editionId}/${firstSha256}/${editionId}.mmdb")
            if (dbFile.exists()) {
                dbFile.path
            } else {
                null
            }
        } catch(e) {
            null
        }
    }

}
