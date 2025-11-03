package application.models;

import application.operation.FileOperation;

import java.util.List;
import java.util.stream.Collectors;

import java.nio.file.Path;
import java.nio.file.Paths;

public record CompileModel(String root, FileOperation op) {
}
