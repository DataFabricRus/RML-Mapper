package be.ugent.mmlab.rml.mapdochandler.extraction.source.concrete;

import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdSourceExtractor;
import be.ugent.mmlab.rml.model.Source;
import be.ugent.mmlab.rml.model.source.std.StdInSource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;

import java.util.HashSet;
import java.util.Set;

public class StdInExtractor extends StdSourceExtractor {
    @Override
    public Set<Source> extractSources(Repository repository, Value resource) {
        Set<Source> inputSources = new HashSet<>();
        inputSources.add(new StdInSource("stdin source", "stdin source"));
        return inputSources;
    }
}
