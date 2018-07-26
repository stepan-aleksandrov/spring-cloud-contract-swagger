package blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.builder;

import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.builder.reference.ReferenceResolver;
import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.builder.reference.ReferenceResolverFactory;
import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.exception.SwaggerContractConverterException;
import blog.svenbayer.springframework.cloud.contract.verifier.spec.swagger.valuefields.SwaggerFields;
import io.swagger.models.Model;
import io.swagger.models.parameters.BodyParameter;

import java.util.Map;

/**
 * Creates the value for a request body.
 *
 * @author Sven Bayer
 */
public final class RequestBodyParamBuilder {

	private static ReferenceResolverFactory refFactory = new ReferenceResolverFactory();

	/**
	 * Creates the value for a request body.
	 *
	 * @param param the request body
	 * @param definitions the Swagger model definitions
	 * @return the value for the request body
	 */
	public static String createValueForRequestBody(BodyParameter param, Map<String, Model> definitions) {
		// TODO this is not verified
		if (param.getExamples() != null && param.getExamples().entrySet().iterator().hasNext()) {
			return String.valueOf(param.getExamples().entrySet().iterator().next());
		} else if (param.getVendorExtensions() != null && param.getVendorExtensions().get(SwaggerFields.X_EXAMPLE.field()) != null) {
			return String.valueOf(param.getVendorExtensions().get(SwaggerFields.X_EXAMPLE.field()));
		} else if (param.getSchema() != null) {
			if (param.getSchema().getExample() != null) {
				return String.valueOf(param.getSchema().getExample());
			} else if (param.getSchema().getVendorExtensions() != null && param.getSchema().getVendorExtensions().get(SwaggerFields.X_EXAMPLE.field()) != null) {
				return String.valueOf(param.getSchema().getVendorExtensions().get(SwaggerFields.X_EXAMPLE.field()));
			} else if (param.getSchema().getReference() != null) {
				String reference = param.getSchema().getReference();
				ReferenceResolver referenceResolver = refFactory.getReferenceResolver(reference);
				return referenceResolver.resolveReference(reference, definitions);
			}
		}
		throw new SwaggerContractConverterException("Could not parse body for request");
	}
}
