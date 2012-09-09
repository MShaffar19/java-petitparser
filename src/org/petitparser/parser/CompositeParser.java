package org.petitparser.parser;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import org.petitparser.Parsers;
import org.petitparser.utils.Transformations;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

/**
 * Helper to compose complex grammars from various primitive parsers. To create
 * a new composite grammar subclass {@link CompositeParser}. Override the method
 * {@link #initialize} and for every production call {@link #def} giving the
 * parsers a name. The start production must be named 'start'. To refer to other
 * productions use {@link #ref}. To redefine or attach actions to productions
 * use {@link #redef} and {@link #action}.
 *
 * @author Lukas Renggli (renggli@gmail.com)
 */
public abstract class CompositeParser extends DelegateParser {

  private final Map<String, Parser> defined = Maps.newHashMap();
  private final Map<String, DelegateParser> undefined = Maps.newHashMap();

  /**
   * Default constructor that triggers the code building the composite grammar.
   */
  public CompositeParser() {
    super(Parsers.epsilon());
    initialize();
    replace(getDelegate(), reference("start"));
    for (Map.Entry<String, DelegateParser> entry : undefined.entrySet()) {
      checkState(defined.containsKey(entry.getKey()), "Undefined production: ", entry.getKey());
      entry.getValue().replace(entry.getValue().getDelegate(), defined.get(entry.getKey()));
    }
    replace(getDelegate(), Transformations.removeDelegates(reference("start")));
  }

  /**
   * Initializes the composite grammar.
   */
  protected abstract void initialize();

  /**
   * Returns a reference to the production with the given {@code name}.
   */
  protected final Parser reference(String name) {
    if (undefined.containsKey(name)) {
      return undefined.get(name);
    } else {
      DelegateParser parser = new DelegateParser(
          Parsers.failure("Uninitalized production: " + name));
      undefined.put(name, parser);
      return parser;
    }
  }

  /**
   * Defines a production with a {@code name} and a {@code parser}.
   */
  protected final void define(String name, Parser parser) {
    checkState(!defined.containsKey(name), "Duplicate production: ", name);
    defined.put(name, parser);
  }

  /**
   * Redefines an existing production with a {@code name} and a new
   * {@code parser}.
   */
  protected final void redefine(String name, Parser parser) {
    checkState(defined.containsKey(name), "Undefined production: ", name);
    defined.put(name, parser);
  }

  /**
   * Redefines an existing production with a {@code name} and a {@code function}
   * producing a new parser.
   */
  protected final void redefine(String name, Function<Parser, Parser> function) {
    redefine(name, function.apply(defined.get(name)));
  }

  /**
   * Attaches an action {@code function} to an existing production {@code name}.
   */
  protected final <S, T> void action(String name, final Function<S, T> function) {
    redefine(name, new Function<Parser, Parser>() {
      @Override
      public Parser apply(Parser parser) {
        return parser.map(function);
      }
    });
  }

}
