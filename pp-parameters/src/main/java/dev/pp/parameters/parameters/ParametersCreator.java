package dev.pp.parameters.parameters;

import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.MutableOrImmutableParameterSpecs;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.token.TextToken;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParametersCreator {

    private record Reference<V> ( V ref ) {}

    public static <V> @Nullable Parameters<V> createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TextToken startToken,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs ) throws TextErrorException {

        return createFromStringParameters (
            stringMap == null ? null : Parameters.createFromStringMap ( stringMap, startToken ),
            parameterSpecs );
    }

    public static <V> @Nullable Parameters<V> createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TextToken startToken,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        return createFromStringParameters (
            stringMap == null ? null : Parameters.createFromStringMap ( stringMap, startToken ),
            parameterSpecs,
            errorHandler );
    }

    public static <V> @Nullable Parameters<V> createFromStringParameters (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs ) throws TextErrorException {

        return createFromStringParameters (
            stringParameters,
            stringParameters == null ? null : stringParameters.getStartToken(),
            parameterSpecs,
            null );
    }

    public static <V> @Nullable Parameters<V> createFromStringParameters (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable TextToken explicitStartToken,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs ) throws TextErrorException {

        return createFromStringParameters_ ( stringParameters, explicitStartToken, parameterSpecs, null );
    }

    public static <V> @Nullable Parameters<V> createFromStringParameters (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        return createFromStringParameters (
            stringParameters,
            stringParameters == null ? null : stringParameters.getStartToken(),
            parameterSpecs,
            errorHandler );
    }

    public static <V> @Nullable Parameters<V> createFromStringParameters (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable TextToken explicitStartToken,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        try {
            return createFromStringParameters_ ( stringParameters, explicitStartToken, parameterSpecs, errorHandler );
        } catch ( TextErrorException e ) {
            throw new RuntimeException ( "Internal error. The following error should have been handled by the error handler" + e );
        }
    }

    private static <V> @Nullable Parameters<V> createFromStringParameters_ (
        final @Nullable MutableOrImmutableParameters<String> stringParameters,
        final @Nullable TextToken explicitStartToken,
        final @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs,
        final @Nullable TextInspectionMessageHandler errorHandler ) throws TextErrorException {

        @Nullable TextToken startToken = explicitStartToken;
        if ( startToken == null && stringParameters != null ) {
            startToken = stringParameters.getStartToken();
        }

        if ( ! checkConsistency ( stringParameters, startToken, parameterSpecs, errorHandler ) ) return null;

        if ( parameterSpecs == null ) return null;

        final @Nullable Set<String> names = stringParameters == null ? null : stringParameters.names();
        final @Nullable Set<String> remainingNames = names == null ? null : new HashSet<> ( names );

        final MutableParameters<V> parameters = new MutableParameters<> ( startToken );

        for ( ParameterSpec<V> parameterSpec : parameterSpecs.listSortedByIndex () ) {
            final @Nullable Parameter<String> stringParameter = stringParameterForParameterSpec (
                stringParameters, parameterSpec, remainingNames );
            final @Nullable Reference<V> value = valueForParameterSpec (
                parameterSpec, stringParameter, errorHandler, startToken );
            if ( value != null ) {
                final Parameter<V> parameter = new Parameter<> (
                    parameterSpec.getName(), value.ref, parameterSpec,
                    stringParameter == null ? null : stringParameter.getNameLocation(),
                    stringParameter == null ? null : stringParameter.getValueLocation() );
                parameters.add ( parameter );
            }
        }

        checkInvalidParameters ( stringParameters, remainingNames, parameterSpecs, errorHandler );

        return parameters.makeImmutableOrNull();
    }

    private static boolean checkConsistency (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable TextToken startToken,
        @Nullable MutableOrImmutableParameterSpecs<?> parameterSpecs,
        @Nullable TextInspectionMessageHandler errorHandler ) throws TextErrorException {

        if ( parameterSpecs == null && stringParameters != null ) {
            handleError (
                "There are no parameters allowed in this context.",
                "NO_PARAMETERS_ALLOWED",
                stringParameters.getStartToken(),
                errorHandler );
            return false;
        } else if ( parameterSpecs != null && stringParameters == null ) {
            if ( parameterSpecs.hasRequiredParameters() ) {
                Set<String> requiredParameters = parameterSpecs.sortedRequiredParameterNames();
                assert requiredParameters != null;
                String message =
                    requiredParameters.size() == 1
                        ? "The following parameter is required: " + requiredParameters.iterator().next()
                        : "The following parameters are required: " + parameterSpecs.sortedRequiredParameterNamesAsString() + ".";
                handleError (
                    message,
                    "PARAMETERS_REQUIRED",
                    startToken,
                    errorHandler );
                return false;
            }
        }

        return true;
    }

    public static void checkInvalidParameters (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @Nullable Set<String> remainingNames,
        @Nullable MutableOrImmutableParameterSpecs<?> parameterSpecs,
        @Nullable TextInspectionMessageHandler errorHandler ) throws TextErrorException {

        if ( remainingNames == null || remainingNames.isEmpty() ) return;

        assert stringParameters != null;
        for ( String name : remainingNames ) {
            Parameter<String> parameter = stringParameters.parameter ( name );
            assert parameterSpecs != null;
            handleError (
                "Parameter '" + name + "' does not exist. The following parameters are valid: " +
                    parameterSpecs.sortedNamesAsString (),
                "INVALID_PARAMETER",
                parameter.nameToken(),
                errorHandler );
        }
    }

    private static <V> @Nullable Reference<V> valueForParameterSpec (
        @NotNull ParameterSpec<V> parameterSpec,
        @Nullable Parameter<String> stringParameter,
        @Nullable TextInspectionMessageHandler errorHandler,
        @Nullable TextToken startToken ) throws TextErrorException {

        String parameterName = parameterSpec.getName();

        if ( stringParameter != null ) {
            try {
                return new Reference<> ( parameterSpec.parse (
                    stringParameter.getValue(), stringParameter.valueOrElseNameLocation() ) );
            } catch ( TextErrorException e ) {
                handleError (
                    e.getMessage(), e.getTextError().getId(), stringParameter.valueToken(), errorHandler );
                return null;
            }

        } else {
            if ( parameterSpec.getDefaultValueSupplier() != null ) {
                return new Reference<> ( parameterSpec.defaultValue () );
            } else {
                handleError (
                    "Parameter '" + parameterName + "' is required.",
                    "MISSING_PARAMETER",
                    startToken,
                    errorHandler );
                return null;
            }
        }
    }

    private static @Nullable Parameter<String> stringParameterForParameterSpec (
        @Nullable MutableOrImmutableParameters<String> stringParameters,
        @NotNull ParameterSpec<?> parameterSpec,
        @Nullable Set<String> remainingNames ) {

        if ( stringParameters == null ) return null;

        Parameter<String> stringParameter = stringParameters.parameterForAnyNameOrNull ( parameterSpec.allNames() );
        if ( stringParameter != null ) {
            assert remainingNames != null;
            remainingNames.remove ( stringParameter.getName() );
        }
        return stringParameter;

/*
        for ( String name : parameterSpec.allNames () ) {
            Parameter<String> stringParameter = stringParameters.parameterOrNull ( name );
            if ( stringParameter != null ) {
                assert remainingNames != null;
                remainingNames.remove ( name );
                return stringParameter;
            }
        }

        return null;
 */
    }

    private static void handleError (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token,
        @Nullable TextInspectionMessageHandler errorHandler ) throws TextErrorException {

        TextError error = new TextError ( message, id, token );
        if ( errorHandler != null ) {
            errorHandler.handleMessage ( error );
        } else {
            throw new TextErrorException ( error );
        }
    }
}
