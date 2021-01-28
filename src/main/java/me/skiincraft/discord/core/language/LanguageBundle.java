package me.skiincraft.discord.core.language;

import me.skiincraft.discord.core.CoreStarter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LanguageBundle {

    protected final Locale locale;
    private final ResourceBundle resourceBundle;
    private static ClassLoader classLoader;

    public LanguageBundle(Locale locale, Path languagePath) {
        this.locale = locale;
        this.createBaseFile(languagePath);
        this.generateFile(languagePath);
        this.resourceBundle = ResourceBundle.getBundle("language", locale, Objects.requireNonNull(generateClassLoader(languagePath)), new UTF8BundleControl());
    }

    public Locale getLocale() {
        return locale;
    }

    public String getString(String key){
        if (resourceBundle.containsKey(key)){
            return resourceBundle.getString(key).replace("{l}", "\n");
        }
        CoreStarter.getLogger().error(String.format("Tradução não foi encontrada. key=%s, locale=%s", key, resourceBundle.getLocale()));
        return "Translation not found";
    }

    public String getString(@Nonnull String... key){
        if (resourceBundle.containsKey(String.join(".", key))){
            return resourceBundle.getString(String.join(".", key));
        }
        CoreStarter.getLogger().error(String.format("Tradução não foi encontrada. key=%s, locale=%s", String.join(".", key), resourceBundle.getLocale()));
        return "Translation not found";
    }

    public String replace(String string){
        List<String> elements;
        try {
            elements = findElements(string);
        } catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
            return string;
        }

        if (elements == null)
            return string;

        elements.removeAll(elements.stream().filter(str -> str.matches("[$-/:-?{-~!\"^`\\s]"))
                .collect(Collectors.toList()));

        if (elements.size() == 0)
            return string;

        for (String str : elements){
            if (findArray(str) != -1){
                int i = findArray(str);
                string = string.replace("${" + str + "}", getStrings(str.toLowerCase().replace("[" + i + "]", ""))[i]);
                continue;
            }
            string = string.replace("${" + str + "}", getString(str.toLowerCase()));
        }
        return string;
    }

    private int findArray(String str) {
        Pattern pattern = Pattern.compile("(\\[\\d+])");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).replaceAll("\\D+", ""));
        }
        return -1;
    }

    @Nullable
    public List<String> findElements(String string) {
        return findElements(string, "${", "}");
    }

    @Nullable
    public List<String> findElements(String string, String delimiterStart, String delimiterEnd){
        if (string == null || !string.contains(delimiterStart)) {
            return Collections.emptyList();
        }
        List<String> strings = new ArrayList<>();
        int start = string.indexOf(delimiterStart);
        String reference = string;
        while (start != -1) {
            reference = reference.substring(start + delimiterStart.length());
            int end = reference.indexOf(delimiterEnd);
            if (end == -1){
                throw new StringIndexOutOfBoundsException(String.format("Está faltando um '%s' em algum token! > %s", delimiterEnd, reference));
            }

            String token = reference.substring(0, end);
            strings.add(token);
            start = reference.indexOf(delimiterStart);
        }
        return strings;
    }

    public String[] getStrings(String key){
        if (resourceBundle.containsKey(key)){
            return resourceBundle.getString(key).split("\\{l}");
        }
        CoreStarter.getLogger().error(String.format("Tradução não foi encontrada (Array). key=%s, locale=%s", key, resourceBundle.getLocale()));
        return new String[] {"Translation not found", locale.toLanguageTag(), key};
    }

    private void generateFile(Path path) {
        try {
            Path language = Paths.get(String.format("%s/language_%s.properties", path.toAbsolutePath(), getLocale().toLanguageTag().replace("-", "_")));
            if (!Files.exists(language)) {
                Files.createFile(language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createBaseFile(Path path) {
        try {
            Path language = Paths.get(String.format("%s/language.properties", path.toAbsolutePath()));
            if (!Files.exists(language)) {
                Files.createFile(language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClassLoader generateClassLoader(Path path){
        if (classLoader != null){
            return classLoader;
        }
        try {
            URL url = path.toAbsolutePath().toUri().toURL();
            return classLoader = new URLClassLoader(new URL[] {url});
        } catch (MalformedURLException ignored){}
        return null;
    }
}
