package dev.pp.parameters.parameter.list;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.token.TextToken;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParametersCreator {

    private static final Object INVALID_OBJECT = new Object();

    /* TODO
    public static <T> Parameters<T> createFromCommandLineParameters (
        List<String> commandLineParameters,
        FormalParameters<T> formalParameters ) {

        return null;
    }
    */

    public static @Nullable Parameters createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TextToken startToken,
        @Nullable FormalParameters formalParameters ) throws TextErrorException {

        return createFromTextParameters (
            stringMap != null ? new TextTokenParameters ( stringMap, startToken ) : null, null,
            formalParameters );
    }

    public static @Nullable Parameters createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TextToken startToken,
        @Nullable FormalParameters formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        return createFromTextParameters (
            stringMap != null ? new TextTokenParameters ( stringMap, startToken ) : null, null,
            formalParameters,
            errorHandler );
    }

    public static @Nullable Parameters createFromTextParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken explicitStartToken,
        @Nullable FormalParameters formalParameters ) throws TextErrorException {

        return createFromTextParameters_ ( textParameters, explicitStartToken, formalParameters, null );
    }

    public static @Nullable Parameters createFromTextParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken explicitStartToken,
        @Nullable FormalParameters formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            return createFromTextParameters_ ( textParameters, explicitStartToken, formalParameters, errorHandler );
        } catch ( TextErrorException e ) {
            throw new RuntimeException ( "Internal error. The following error should have been handled by the error handler" + e );
        }
    }

    private static @Nullable Parameters createFromTextParameters_ (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken explicitStartToken,
        @Nullable FormalParameters formalParameters,
        @Nullable TextErrorHandler errorHandler ) throws TextErrorException {

        @Nullable TextToken startToken = explicitStartToken;
        if ( startToken == null && textParameters != null ) {
            startToken = textParameters.getStartToken();
        }

        if ( ! checkConsistency ( textParameters, startToken, formalParameters, errorHandler ) ) return null;

        if ( formalParameters == null ) return null;

        @Nullable Set<String> remainingNames =
            textParameters != null ? new HashSet<> ( textParameters.getNames() ) : null;

        Parameters parameters = new Parameters ( textParameters );

        for ( FormalParameter<?> formalParameter : formalParameters.getAllSortedByIndex () ) {
            @Nullable Object value = getValueForFormalParameter (
                formalParameter, textParameters, remainingNames, errorHandler, startToken );
            if ( value != INVALID_OBJECT ) {
                parameters.add ( formalParameter.getName (), value );
            }
        }

        checkInvalidParameters ( textParameters, remainingNames, formalParameters, errorHandler );

        return parameters;
    }
/*
    public static boolean checkConsistency (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable FormalParameters<?> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        if ( formalParameters == null && textParameters != null ) {
            errorHandler.handleAbortingError (
                "NO_PARAMETERS_ALLOWED",
                "There are no parameters allowed in this context.",
                textParameters.getStartToken() );
            return false;
        } else if ( formalParameters != null && textParameters == null ) {
            if ( formalParameters.hasRequiredParameters() ) {
                errorHandler.handleAbortingError (
                    "PARAMETERS_REQUIRED",
                    "The following parameters are required: " +
                        formalParameters.sortedRequiredParameterNamesAsString() + ".",
                    textParametersStartToken );
                return false;
            }
        }
        return true;
    }

 */

    private static boolean checkConsistency (
        @Nullable TextTokenParameters parameters,
        @Nullable TextToken startToken,
        @Nullable FormalParameters formalParameters,
        @Nullable TextErrorHandler errorHandler ) throws TextErrorException {

        if ( formalParameters == null && parameters != null ) {
            handleError (
                "NO_PARAMETERS_ALLOWED",
                "There are no parameters allowed in this context.",
                parameters.getStartToken(),
                errorHandler );
            return false;
        } else if ( formalParameters != null && parameters == null ) {
            if ( formalParameters.hasRequiredParameters() ) {
                Set<String> requiredParameters = formalParameters.sortedRequiredParameterNames();
                assert requiredParameters != null;
                String message =
                    requiredParameters.size() == 1
                    ? "The following parameter is required: " + requiredParameters.iterator().next()
                    : "The following parameters are required: " + formalParameters.sortedRequiredParameterNamesAsString() + ".";
                handleError (
                    "PARAMETERS_REQUIRED",
                    message,
                    startToken,
                    errorHandler );
                return false;
            }
        }

        return true;
    }

    public static void checkInvalidParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @Nullable FormalParameters formalParameters,
        @Nullable TextErrorHandler errorHandler ) throws TextErrorException {

        if ( remainingNames == null || remainingNames.isEmpty() ) return;

        assert textParameters != null;
        for ( String name : remainingNames ) {
            TextTokenParameter parameter = textParameters.get ( name );
            assert formalParameters != null;
            handleError (
                "INVALID_PARAMETER",
                "Parameter '" + name + "' does not exist. The following parameters are valid: " +
                    formalParameters.sortedParameterNamesAsString(),
                parameter.getNameToken(),
                errorHandler );
        }
    }

    private static @Nullable Object getValueForFormalParameter (
        @NotNull FormalParameter<?> formalParameter,
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @Nullable TextErrorHandler errorHandler,
        @Nullable TextToken startToken ) throws TextErrorException {

        String parameterName = formalParameter.getName();

        TextTokenParameter textParameter = getTextParameterForFormalParameter (
            textParameters, formalParameter, remainingNames );

        if ( textParameter != null ) {
            try {
                return formalParameter.parse (
                    textParameter.getValue(), textParameter.getValueOrElseNameToken() );
            } catch ( TextErrorException e ) {
                handleError (
                    e.getId(), e.getMessage(), textParameter.getValueToken(), errorHandler );
                return INVALID_OBJECT;
            }

        } else {
            if ( formalParameter.getDefaultValueSupplier() != null ) {
                return formalParameter.getDefaultValue();
            } else {
                handleError (
                    "MISSING_PARAMETER",
                    "Parameter '" + parameterName + "' is required.",
                    startToken,
                    errorHandler );
                return INVALID_OBJECT;
            }
        }
    }

    private static TextTokenParameter getTextParameterForFormalParameter (
        @Nullable TextTokenParameters textParameters,
        @NotNull FormalParameter<?> formalParameter,
        @Nullable Set<String> remainingNames ) {

        if ( textParameters == null ) return null;

        for ( String name : formalParameter.getAllNames() ) {
            TextTokenParameter parameter = textParameters.getOrNull ( name );
            if ( parameter != null ) {
                assert remainingNames != null;
                remainingNames.remove ( name );
                return parameter;
            }
        }

        return null;
    }

    private static void handleError (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable TextErrorHandler errorHandler ) throws TextErrorException {

        if ( errorHandler != null ) {
            errorHandler.handleNonAbortingError ( id, message, token );
        } else {
            throw new TextErrorException ( id, message, token );
        }
    }
}
