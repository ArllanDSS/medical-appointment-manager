# 🏥 MedSync - Sistema de Gestão de Consultas Médicas

O **MedSync** é uma aplicação Java Core desenvolvida para gerenciar o fluxo de agendamentos de uma clínica médica. O projeto foca na aplicação de regras de negócio complexas, persistência robusta com JDBC e organização de código seguindo padrões de mercado.

---

## 🛠️ Tecnologias e Ferramentas

* **Java 17+**: Linguagem base do sistema.
* **PostgreSQL**: Banco de dados relacional.
* **JDBC**: Interface para comunicação com o banco de dados.
* **Properties File**: Gestão de configurações e segurança de credenciais.

---

## 🏗️ Arquitetura do Sistema

O projeto foi estruturado seguindo a separação de responsabilidades (SoC):

1.  **Model**: Classes de domínio (`Medico`, `Paciente`, `Consulta`).
2.  **Repository (DAO)**: Camada de persistência com SQL puro.
3.  **Service**: Camada de inteligência, onde residem as regras de negócio.
4.  **View (CLI)**: Interface interativa via terminal com tabelas formatadas.
5.  **Exception**: Tratamento de erros personalizado com `ValidacaoException`.

---

## ⚖️ Regras de Negócio e Validações

O sistema garante a integridade dos agendamentos através de validações automáticas:

* **Horário Clínico**: Agendamentos permitidos apenas de segunda a sábado, das 08:00 às 18:00.
* **Antecedência**: Bloqueio de agendamentos para datas passadas.
* **Conflito de Médicos**: Impede que um médico tenha duas consultas no mesmo horário.
* **Restrição de Pacientes**: O sistema não permite que um paciente agende mais de uma consulta no mesmo dia.
* **Status de Ativação**: Apenas médicos e pacientes ativos podem realizar novos agendamentos.

---

## 🚀 Como Executar o Projeto

### 1. Banco de Dados
Crie um banco chamado `medsync_db` e execute os scripts na pasta `/sql`:
1.  `schema.sql`: Cria as tabelas e relacionamentos.
2.  `data.sql`: Popula o sistema com massa de dados inicial.

### 2. Configuração de Segurança (Credenciais)
O projeto utiliza um arquivo `.properties` para proteger dados sensíveis:
1.  Localize o arquivo `config.properties.example` na raiz do projeto.
2.  Renomeie-o para `config.properties`.
3.  Preencha com suas credenciais locais do PostgreSQL.
    *Nota: O arquivo `config.properties` está no `.gitignore` por motivos de segurança.*

### 3. Execução
Execute a classe `Main.java` e interaja com o menu no terminal.

---

## 📈 Próximos Passos
Este projeto servirá como base para a migração para **Spring Boot**, evoluindo para uma API RESTful com Spring Data JPA e Spring Security.

---
**Desenvolvido por Arllan**