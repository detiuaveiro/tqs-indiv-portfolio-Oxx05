Aula 6

- Cucumber

É criado um file **.feature** que descreve cenários de testes em uma linguagem chamada Gherkin.

Palavras-chave do Gherkin:
- Feature: descreve a funcionalidade que está sendo testada.
- Scenario: descreve um cenário específico dentro da funcionalidade.
- Given: define o estado inicial do sistema antes da execução do cenário.
- When: descreve a ação ou evento que ocorre no cenário.
- Then: descreve o resultado esperado após a ação ou evento.
- And / But: usadas para adicionar mais condições ou ações em um cenário.

Para fazer a transição de Gherkin para teste usa - se um ficheiro geralmente nomeado Steps Definitions onde são implementados os passos definidos no ficheiro .feature.
Neste ficheiro é feita a tradução dos cenários para código com por exemplo:

______________________________________________________________________________

@When("I search for books published between {string} and {string}")
public void searchByDateRange(String from, String to) {
results = library.findBooks(LocalDateTime.parse(from, formatter), LocalDateTime.parse(to, formatter));
}

@Then("I should get {int} books")
public void i_should_get_books(int expectedCount) {
    assertThat(results.size()).isEqualTo(expectedCount);
}

______________________________________________________________________________


