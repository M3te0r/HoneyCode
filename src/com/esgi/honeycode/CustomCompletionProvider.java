package com.esgi.honeycode;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

/**
 * Create a CompletionProvider
 */
public class CustomCompletionProvider {

    private CompletionProvider provider;

    /**
     * Constructeur de la classe pour le langage précisé
     * @param languageType langage à compléter
     */
    public CustomCompletionProvider(String languageType) {

        this.provider = createProvider(languageType);
    }

    /**
     * Retourne le fournisseur de complétion
     * @return this
     */
    public CompletionProvider getProvider() {
        return this.provider;
    }

    /**
     * Créer un fournisseur de complétion par défaut, personnalisé pour Java
     * @param languageType langage
     * @return le fournisseur
     */
    private DefaultCompletionProvider createProvider(String languageType)
    {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        if(languageType.equals("java"))
        {
            provider.addCompletion(new BasicCompletion(provider, "abstract"));
            provider.addCompletion(new BasicCompletion(provider, "assert"));
            provider.addCompletion(new BasicCompletion(provider, "break"));
            provider.addCompletion(new BasicCompletion(provider, "case"));
            provider.addCompletion(new BasicCompletion(provider, "catch"));
            provider.addCompletion(new BasicCompletion(provider, "class"));
            provider.addCompletion(new BasicCompletion(provider, "const"));
            provider.addCompletion(new BasicCompletion(provider, "continue"));
            provider.addCompletion(new BasicCompletion(provider, "default"));
            provider.addCompletion(new BasicCompletion(provider, "do"));
            provider.addCompletion(new BasicCompletion(provider, "else"));
            provider.addCompletion(new BasicCompletion(provider, "enum"));
            provider.addCompletion(new BasicCompletion(provider, "extends"));
            provider.addCompletion(new BasicCompletion(provider, "final"));
            provider.addCompletion(new BasicCompletion(provider, "finally"));
            provider.addCompletion(new BasicCompletion(provider, "for"));
            provider.addCompletion(new BasicCompletion(provider, "goto"));
            provider.addCompletion(new BasicCompletion(provider, "if"));
            provider.addCompletion(new BasicCompletion(provider, "int"));
            provider.addCompletion(new BasicCompletion(provider, "implements"));
            provider.addCompletion(new BasicCompletion(provider, "import"));
            provider.addCompletion(new BasicCompletion(provider, "instanceof"));
            provider.addCompletion(new BasicCompletion(provider, "interface"));
            provider.addCompletion(new BasicCompletion(provider, "native"));
            provider.addCompletion(new BasicCompletion(provider, "new"));
            provider.addCompletion(new BasicCompletion(provider, "package"));
            provider.addCompletion(new BasicCompletion(provider, "private"));
            provider.addCompletion(new BasicCompletion(provider, "protected"));
            provider.addCompletion(new BasicCompletion(provider, "public"));
            provider.addCompletion(new BasicCompletion(provider, "return"));
            provider.addCompletion(new BasicCompletion(provider, "static"));
            provider.addCompletion(new BasicCompletion(provider, "strictfp"));
            provider.addCompletion(new BasicCompletion(provider, "super"));
            provider.addCompletion(new BasicCompletion(provider, "switch"));
            provider.addCompletion(new BasicCompletion(provider, "synchronized"));
            provider.addCompletion(new BasicCompletion(provider, "this"));
            provider.addCompletion(new BasicCompletion(provider, "throw"));
            provider.addCompletion(new BasicCompletion(provider, "throws"));
            provider.addCompletion(new BasicCompletion(provider, "transient"));
            provider.addCompletion(new BasicCompletion(provider, "try"));
            provider.addCompletion(new BasicCompletion(provider, "void"));
            provider.addCompletion(new BasicCompletion(provider, "volatile"));
            provider.addCompletion(new BasicCompletion(provider, "while"));
        }

        //Next conditions for plugins

        return provider;

    }
}
