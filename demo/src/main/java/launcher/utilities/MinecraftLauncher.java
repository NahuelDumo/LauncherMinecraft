package launcher.utilities;

import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftLauncher {

    private static File appDataDirectory;

    public static void main(String[] args, String playerName) {
        String appDataPath = System.getenv("APPDATA");
        appDataDirectory = new File(appDataPath);
        String minecraftDirectory = appDataDirectory + "/.minecraft";

        String minecraftVersion = detectMinecraftVersion(minecraftDirectory);
        if (minecraftVersion != null) {
            launchMinecraft(playerName, minecraftVersion);
        } else {
            System.out.println("No se pudo detectar la versión de Minecraft.");
        }
    }

    private static String detectMinecraftVersion(String minecraftDirectory) {
        File versionsDir = new File(minecraftDirectory + "/versions");
        File[] versionFiles = versionsDir.listFiles();
        if (versionFiles != null && versionFiles.length > 0) {
            return versionFiles[1].getName(); // Cambiar el índice según la versión que quieras
        }
        return null;
    }

    private static void launchMinecraft(String playerName, String version) {
        String gameDirectory = appDataDirectory + "/.minecraft";

        // Generar un UUID único para el jugador (esto puede variar según tu implementación)
        UUID playerUUID = UUID.randomUUID();

        // Autenticar al jugador y obtener el token de acceso
        String accessToken = generateAccessToken(playerName);

        // Crear AuthInfos con el token de acceso y el UUID
        AuthInfos authInfos = new AuthInfos(playerName, accessToken, playerUUID.toString());

        String[] parts = version.split("-");

        // Crear un objeto GameVersion y un arreglo GameTweak adecuados
        GameVersion version2 = new GameVersion(parts[0], GameType.V1_13_HIGHER_FORGE);
        GameTweak[] tweaks = { GameTweak.FORGE }; // Ajustes adecuados

        // Usar los objetos en el constructor de GameInfos
        GameInfos gameInfos = new GameInfos("MyMinecraft", version2, tweaks);

        // Asegúrate de tener los valores correctos para cada argumento
        String mainClass = "net.minecraft.client.main.Main";
        String classPath = gameDirectory + "/versions/" + version + "/" + version + ".jar"; // Cambiar la ruta según la versión
        List<String> vmArgs = new ArrayList<>();
        List<String> launchArgs = new ArrayList<>();
        boolean redirectErrorStream = false; // Puedes cambiar esto según tus necesidades
        String macDockName = null; // Puedes dejarlo como null si no es relevante
        File directory = new File(gameDirectory);

        // Crear el perfil de lanzamiento externo
        ExternalLaunchProfile profile = new ExternalLaunchProfile(mainClass, classPath, vmArgs, launchArgs, redirectErrorStream, macDockName, directory);
        ExternalLauncher launcher = new ExternalLauncher(profile);
        System.out.println("hola " +launcher.getProfile().getArgs().toString()); 

        try {
            launcher.launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateAccessToken(String playerName) {
        // Generar un UUID único para el jugador
        UUID playerUUID = UUID.randomUUID();

        // Convertir el UUID en una cadena para el token de acceso
        String accessToken = playerUUID.toString();

        return accessToken;
    }
}
/*
 * cambios pendientes: 
 * -ejecutar minecraft con archivos alojados dentro de la carpeta .minecraft
 */