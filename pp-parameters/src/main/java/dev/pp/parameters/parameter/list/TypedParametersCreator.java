package dev.pp.parameters.parameter.list;

@Deprecated
public class TypedParametersCreator {

    /* TODO
    public static <T> Parameters<T> createFromCommandLineParameters (
        List<String> commandLineParameters,
        FormalParameters<T> formalParameters ) {

        return null;
    }
    */

/*
    public static @Nullable <T> TypedParameters<T> createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TypedFormalParameters<T> formalParameters ) throws DataValidatorException {

        return createFromTextParameters (
            stringMap != null ? new TextTokenParameters ( stringMap, null ) : null,
            null,
            formalParameters );
    }

    public static @Nullable <T> TypedParameters<T> createFromStringMap (
        @Nullable Map<String, String> stringMap,
        @Nullable TypedFormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        return createFromTextParameters (
            stringMap != null ? new TextTokenParameters ( stringMap, null ) : null,
            null,
            formalParameters,
            errorHandler );
    }

    public static @Nullable <T> TypedParameters<T> createFromTextParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable TypedFormalParameters<T> formalParameters ) throws DataValidatorException {

        return createFromTextParameters_ ( textParameters, textParametersStartToken, formalParameters, null );
    }

    public static @Nullable <T> TypedParameters<T> createFromTextParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable TypedFormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            return createFromTextParameters_ ( textParameters, textParametersStartToken, formalParameters, errorHandler );
        } catch ( DataValidatorException e ) {
            throw new RuntimeException ( "Internal error. The following error should have been handled by the error handler" + e );
        }
    }

    private static @Nullable <T> TypedParameters<T> createFromTextParameters_ (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable TypedFormalParameters<T> formalParameters,
        @Nullable TextErrorHandler errorHandler ) throws DataValidatorException {

        checkConsistency ( textParameters, textParametersStartToken, formalParameters, errorHandler );

        if ( formalParameters == null ) return null;

        @Nullable Set<String> remainingNames =
            textParameters != null ? new HashSet<> ( textParameters.getNames() ) : null;

        TypedParameters<T> parameters = new TypedParameters<> ();

        for ( FormalParameter<T> formalParameter : formalParameters.getListSortedByOrder() ) {
            Parameter<T> parameter = getParameterForFormalParameter (
                formalParameter, textParameters, remainingNames, errorHandler, textParametersStartToken );
            if ( parameter != null ) parameters.add ( parameter );
        }

        checkInvalidParameters ( textParameters, remainingNames, formalParameters, errorHandler );

        return parameters;
    }

    private static void checkConsistency (
        @Nullable TextTokenParameters parameters,
        @Nullable TextToken startToken,
        @Nullable TypedFormalParameters<?> formalParameters,
        @Nullable TextErrorHandler errorHandler ) throws DataValidatorException {

        @Nullable TextErrorOld error = null;
        if ( formalParameters == null && parameters != null ) {
            handleError (
                "NO_PARAMETERS_ALLOWED",
                "There are no parameters allowed in this context.",
                parameters.getStartToken(),
                errorHandler );
        } else if ( formalParameters != null && parameters == null ) {
            if ( formalParameters.hasRequiredParameters() ) {
                handleError (
                    "PARAMETERS_REQUIRED",
                    "The following parameters are required: " +
                        formalParameters.sortedRequiredParameterNamesAsString() + ".",
                    startToken,
                    errorHandler );
            }
        }
    }

    public static <T> boolean checkInvalidParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @Nullable TypedFormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) throws DataValidatorException {

        if ( remainingNames == null || remainingNames.isEmpty() ) return true;

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

        return false;
    }

    private static <T> Parameter<T> getParameterForFormalParameter (
        @NotNull FormalParameter<T> formalParameter,
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textParametersStartToken ) throws DataValidatorException {

        String parameterName = formalParameter.getName();

        TextTokenParameter textParameter = getTextParameterForFormalParameter (
            textParameters, formalParameter, remainingNames );

        if ( textParameter != null ) {
            try {
                T value = formalParameter.parse (
                    textParameter.getValue(), textParameter.getValueOrElseNameToken() );
                return new Parameter<> ( parameterName, value );
            } catch ( TextErrorException e ) {
                handleError (
                    e.getId(), e.getMessage(), textParameter.getValueToken(), errorHandler );
                return null;
            }

        } else {
            if ( formalParameter.getDefaultValueSupplier() != null ) {
                return new Parameter<> ( parameterName, formalParameter.getDefaultValue() );
            } else {
                handleError (
                    "MISSING_PARAMETER",
                    "Parameter '" + parameterName + "' is required.",
                    textParametersStartToken,
                    errorHandler );
                return null;
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
        @Nullable TextErrorHandler errorHandler ) throws DataValidatorException {

        if ( errorHandler != null ) {
            errorHandler.handleAbortingError ( id, message, token );
        } else {
            throw new DataValidatorException ( id, message, token, null );
        }
    }
*/
}
