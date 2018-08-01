package blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.json;

import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.builder.TestFileResourceLoader;
import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.builder.reference.SwaggerDefinitionsRefResolverSwagger;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonSchemaComparerTest {

	private JsonSchemaComparer jsonSchemaComparer;

	@BeforeEach
	void init() {
		jsonSchemaComparer = new JsonSchemaComparer();
	}

	@DisplayName("Should be true for equal Jsons")
	@Test
	void equalJsons() throws IOException {
		File jsonFile= TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withEqualFields/CoffeeRocket.json");
		String json = new String(Files.readAllBytes(jsonFile.toPath()));
		assertTrue(jsonSchemaComparer.isEquals(json, json), "Same json files should be equals!");
	}

	@DisplayName("Should be true for equal Jsons with different values")
	@Test
	void equalJsonsDifferentValues() throws IOException {
		File expectedJsonFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withDifferentValues/CoffeeRocket1.json");
		String expectedJson = new String(Files.readAllBytes(expectedJsonFile.toPath()));

		File actualJsonFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withDifferentValues/CoffeeRocket2.json");
		String actualJson = new String(Files.readAllBytes(actualJsonFile.toPath()));

		assertTrue(jsonSchemaComparer.isEquals(expectedJson, actualJson), "Same json files with differnet values should be equals!");
	}

	@DisplayName("Should be false for different Json files")
	@Test
	void differentJsonFiles() throws IOException {
		File expectedJsonFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withDifferentJsons/CoffeeRocket1.json");
		String expectedJson = new String(Files.readAllBytes(expectedJsonFile.toPath()));

		File actualJsonFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withDifferentJsons/CoffeeRocket2.json");
		String actualJson = new String(Files.readAllBytes(actualJsonFile.toPath()));

		assertFalse(jsonSchemaComparer.isEquals(expectedJson, actualJson), "Different Jsons should result in false!");
	}

	@DisplayName("Json file and Swagger definitions should be equal")
	@Test
	void equalJsonFileAndSwaggerDefinitions() throws IOException {
		File swaggerFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withEqualFields/external_json_swagger.yml");
		Swagger swagger = new SwaggerParser().read(swaggerFile.getPath());
		Map<String, Model> definitions = swagger.getDefinitions();
		SwaggerDefinitionsRefResolverSwagger swaggerDefinitionsRefResolverSwagger = new SwaggerDefinitionsRefResolverSwagger("#/definitions/CoffeeRocket");
		String expectedJson = swaggerDefinitionsRefResolverSwagger.resolveReference(definitions);

		File jsonFile= TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withEqualFields/CoffeeRocket.json");
		String actualJson = new String(Files.readAllBytes(jsonFile.toPath()));

		assertTrue(jsonSchemaComparer.isEquals(expectedJson, actualJson), "Json from Swagger definitions and Json file should be equal!");
	}

	@DisplayName("Should be not equal for Json with more fields than Swagger definitions")
	@Test
	void withJsonMoreFields() throws IOException {
		File swaggerFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withJsonMoreFields/external_json_swagger.yml");
		Swagger swagger = new SwaggerParser().read(swaggerFile.getPath());
		Map<String, Model> definitions = swagger.getDefinitions();
		SwaggerDefinitionsRefResolverSwagger swaggerDefinitionsRefResolverSwagger = new SwaggerDefinitionsRefResolverSwagger("#/definitions/CoffeeRocket");
		String expectedJson = swaggerDefinitionsRefResolverSwagger.resolveReference(definitions);

		File jsonFile= TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withJsonMoreFields/CoffeeRocket.json");
		String actualJson = new String(Files.readAllBytes(jsonFile.toPath()));

		assertFalse(jsonSchemaComparer.isEquals(expectedJson, actualJson), "Json file with more fields than Swagger definitions should be false!");
	}

	@DisplayName("Should be not equal for Swagger definitions with more fields than Json")
	@Test
	void withSwaggerMoreFields() throws IOException {
		File swaggerFile = TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withSwaggerMoreFields/external_json_swagger.yml");
		Swagger swagger = new SwaggerParser().read(swaggerFile.getPath());
		Map<String, Model> definitions = swagger.getDefinitions();
		SwaggerDefinitionsRefResolverSwagger swaggerDefinitionsRefResolverSwagger = new SwaggerDefinitionsRefResolverSwagger("#/definitions/CoffeeRocket");
		String expectedJson = swaggerDefinitionsRefResolverSwagger.resolveReference(definitions);

		File jsonFile= TestFileResourceLoader.getResourceAsFile("swagger/jsonFileResolver/withSwaggerMoreFields/CoffeeRocket.json");
		String actualJson = new String(Files.readAllBytes(jsonFile.toPath()));

		assertFalse(jsonSchemaComparer.isEquals(expectedJson, actualJson), "Swagger definitions with more fields than Json file should be false!");
	}
}