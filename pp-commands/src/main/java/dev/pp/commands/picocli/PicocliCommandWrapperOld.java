package dev.pp.commands.picocli;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.IExecutionStrategy;

@Deprecated
public interface PicocliCommandWrapperOld extends IExecutionStrategy {

    CommandSpec getCommandSpec();
}
