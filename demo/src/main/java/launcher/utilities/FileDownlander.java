package launcher.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FileDownlander {

    private static File appDataDirectory;


    public static void main(String[] args, String playerName) {
        String userHome = System.getProperty("user.home");
        String appDataPath = System.getenv("APPDATA");
        appDataDirectory = new File(appDataPath);

        if (!hasMinecraftFolder(appDataPath)) {
            System.out.println("No existe la carpeta .minecraft, procediendo a descargarla...");
            if (downloadAndExtractFiles(userHome)) {
                System.out.println(".minecraft descargada y descomprimida exitosamente.");
            } else {
                System.out.println("Error al descargar o descomprimir .minecraft.");
                return;
            }
        } 


        System.out.println("Lanzando Minecraft...");
        lanzarMinecraft(playerName);
    }

    private static void lanzarMinecraft(String playerName) {
        MinecraftLauncher lanzador = new MinecraftLauncher();
        lanzador.main(null, playerName);
    }

    public static boolean hasMinecraftFolder(String userHome) {
        File minecraftFolder = new File(userHome, ".minecraft");
        return minecraftFolder.exists() && minecraftFolder.isDirectory();
    }

    public static boolean downloadAndExtractFiles(String userHome) {
        try {
            String githubRepoUrl = "https://github.com/NahuelDumo/KYRA-STUDIO/archive/main.zip";
            File zipFile = new File(userHome, "kyra.zip");
    
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(githubRepoUrl);
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        try (InputStream in = response.getEntity().getContent();
                             FileOutputStream out = new FileOutputStream(zipFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = in.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }
                    } else {
                        System.out.println("Error al descargar el archivo ZIP.");
                        return false;
                    }
                }
            }

            File downloadsFolder = new File(userHome, "ArchivosServidor");
            extractZip(zipFile, downloadsFolder);



            try {
                 File minecraftFolder = new File(downloadsFolder, "KYRA-STUDIO-main/.minecraft");
                moveFolder(minecraftFolder, appDataDirectory);
                System.out.println("Carpeta .minecraft movida exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al mover la carpeta .minecraft: " + e.getMessage());
            }
    
            try {
                File kyraStudiosFolder = new File(downloadsFolder, "KYRA-STUDIO-main/kyra-studio");
                moveFolder(kyraStudiosFolder, appDataDirectory);
                System.out.println("Carpeta kyra-studios movida exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al mover la carpeta kyra-studios: " + e.getMessage());
            }
            return true;
    
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void extractZip(File zipFile, File destinationFolder) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File outputFile = new File(destinationFolder, entry.getName());
                
                if (!entry.isDirectory()) {
                    outputFile.getParentFile().mkdirs(); // Asegurar que la carpeta padre exista
                    try (OutputStream out = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    outputFile.mkdirs(); // Crear carpeta
                }
            }
        }
    }

    public static void moveFolder(File source, File destination) {
        if (source.exists() && source.isDirectory()) {
            File destinationFolder = new File(destination, source.getName());
            
            if (destinationFolder.exists()) {
                deleteFolder(destinationFolder);
            }
            
            source.renameTo(destinationFolder);
        }
    }
    
    public static void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            folder.delete();
        }
    }
}


/*
 * cambios pendientes: 
 * -realizar comprobacion de archivos iguales dentro de la carpeta .minecraft con la carpeta alojada en servidor
 * Duda pendiente: comprobar instalacion de java es necesario ?
 */