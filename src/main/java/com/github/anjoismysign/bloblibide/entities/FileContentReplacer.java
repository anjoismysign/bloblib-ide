package com.github.anjoismysign.bloblibide.entities;

import com.fasterxml.jackson.databind.ObjectReader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;

public record FileContentReplacer (@NotNull Pattern pattern,
                                   @NotNull String replacement){

    @NotNull
    public static FileContentReplacer of(@NotNull String pattern,
                                         @NotNull String replacement) {
        return new FileContentReplacer(Pattern.compile(Objects.requireNonNull(pattern, "'pattern' cannot be null")),
                Objects.requireNonNull(replacement, "'replacement' cannot be null"));
    }

    @NotNull
    public String matchAndReplace(@NotNull String line) {
        return pattern.matcher(Objects.requireNonNull(line, "'line' cannot be null"))
                .replaceAll(replacement);
    }

    public void matchAndReplace(@NotNull File inputFile,
                                @NotNull File outputFile) {
        try {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(inputFile, "'inputFile' cannot be null")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Objects.requireNonNull(outputFile, "'outputFile' cannot be null")));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(matchAndReplace(line));
                bufferedWriter.newLine();
            }
        } finally {
            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public static String getContent(@NotNull File inputFile) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @NotNull
    public static String getContent(@NotNull InputStream inputStream) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
