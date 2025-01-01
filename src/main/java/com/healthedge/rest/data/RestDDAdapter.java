package com.healthedge.rest.data;

import com.healthedge.config.props.PropertyReader;
import com.healthedge.rest.config.RestConstants;
import com.healthedge.rest.driver.model.RestDDModel;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RestDDAdapter {
    private static final String GLOBAL_CONFIG_MEMBER_NAME = "globalConfig";
    private final LinkedHashMap<String, LinkedHashMap<String, RestDDModel>> scenarioMap = new LinkedHashMap<>();
    private final LinkedList<RestDDModel> scenarioList = new LinkedList();
    private final LinkedList<Path> files = new LinkedList<>();

    public RestDDAdapter() {
        this(getDefaultfolders());
    }

    public RestDDAdapter(String pathToFolder) {
        this(Collections.singletonList(pathToFolder));
    }

    public RestDDAdapter(List<String> pathsToFolder) {
        pathsToFolder.stream().forEach(s -> {
            try {
                Files.walk(Paths.get(RestTestData.ROOT_TESTDATA_PATH, "dd", s))
                        .filter(Files::isRegularFile)
                        .forEach(path -> files.add(path));
            } catch (IOException e) {
                throw new RuntimeException("Error during reading files from folder: " + s, e);
            }
        });
    }

    private static List<String> getDefaultfolders() {
        String folderPaths = PropertyReader.getProperty(RestConstants.Properties.DD_FOLDER_PATHS);
        if (folderPaths.isEmpty()) {
            throw new RuntimeException(RestConstants.Properties.DD_FOLDER_PATHS + " property has not been provided. No tests to run");
        }
        return Arrays.asList(folderPaths.replaceAll("\\s", "").split(",")).stream().collect(Collectors.toList());
    }

    private RestDDAdapter readFile(String fileName) {
        LinkedHashMap<String, RestDDModel> scenarios = new LinkedHashMap<>();
        JsonObject json = new RestTestData().fromFile(fileName).asJsonElement().getAsJsonObject();

        RestDDModel.Step globalConfig = null;
        if (json.has(GLOBAL_CONFIG_MEMBER_NAME)) {
            globalConfig = RestTestData.gsonBuilder.serializeNulls().create().fromJson(json.get(GLOBAL_CONFIG_MEMBER_NAME), RestDDModel.Step.class);
            json.remove(GLOBAL_CONFIG_MEMBER_NAME);
        }
        RestDDModel.Step finalGlobalModel = globalConfig;
        json.keySet().stream().forEach(s -> {
            RestDDModel model = RestTestData.gsonBuilder.serializeNulls().create().fromJson(json.get(s), RestDDModel.class);
            if (null != finalGlobalModel) {
                model = model.merge(finalGlobalModel);
            }
            this.scenarioList.add(model);
            Path file = Paths.get(fileName);
            String relativeFileName = Paths.get(file.getName(file.getNameCount() - 2).toString(), file.getFileName().toString()).toString();
            model.setRelativeFileName(relativeFileName);
            model.setRelativeScenarioName(s);
            scenarios.put(s, model);

            Map<String, RestDDModel.Step> steps = model.getSteps();
            steps.forEach((s1, step) -> {
                if (steps.get(s1).getStepName() == null) {
                    steps.get(s1).setStepName(s1);
                }
            });
        });
        this.scenarioMap.put(fileName, scenarios);
        return this;
    }

    public RestDDAdapter fromSingleFile(String fileName) {
        Path file = files.stream().filter(path -> path.getFileName().toString().contains(fileName)).findFirst().orElseThrow(() ->
                new RuntimeException("Error during file search by filename: " + fileName + ". Please check if file name is properly entered without path or 'dd.folder.paths' parameter is provided or file exists in folder."));
        readFile(file.toString());
        return this;
    }

    public RestDDAdapter fromFiles(List<String> fileNames) {
        fileNames.stream().forEach(s -> fromSingleFile(s));
        return this;
    }

    public RestDDAdapter fromPropertyFiles() {
        String fileNames = PropertyReader.getProperty(RestConstants.Properties.DD_FILE_NAMES);
        if (fileNames.isEmpty()) {
            return fromAllFiles();
        }
        List<String> files = Arrays.asList(fileNames.replaceAll("\\s", "").split(","));
        return fromFiles(files);
    }

    public RestDDAdapter fromAllFiles() {
        files.stream().forEach(path -> readFile(path.toString()));
        return this;
    }

    public LinkedHashMap<String, LinkedHashMap<String, RestDDModel>> getScenarioMap() {
        return scenarioMap;
    }

    public LinkedList<RestDDModel> getScenarioList() {
        return scenarioList;
    }

    public LinkedHashMap<String, RestDDModel> getScenarioMap(String fileName) {
        return scenarioMap.get(fileName);
    }
}
