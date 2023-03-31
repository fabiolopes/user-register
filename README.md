## user-register
API de cadastro de usuários com autenticação via JWT.

Para executar a aplicação, basta executar a classe **UserRegisterApplication**, contida no pacote _com.bios.user_.

Por default, o host após a subida é **http://localhost:8080**. 

Os endpoints base da aplicação são os seguintes:


**POST /user** - Endpoint público neste caso para cadastro de um novo usuário.

* Envio do json com os campos: "name": String, "email": String, "password": String.

Ex.
~~~json
{
  "name": "Pessoa da Silva",
  "email": "pessoa_da_silva@email.com",
  "password": "123456"
}
~~~

Como resposta, temos o seguinte json:

~~~json
{
  "id": 1,
  "name": "Pessoa da Silva",
  "email": "pessoa_da_silva@email.com"
}
~~~

**POST /login** - Endpoint para realizar login. 
* Envio de json com os campos: "email": String, "password": String. Caso retorno positivo, teremos status 200 (OK), bem 
como no header da resposta teremos a chave "Authorization" contendo o token bearer gerado.

Ex. 

~~~json
{
  "email": "pessoa_da_silva@email.com",
  "password": "123456"
}
~~~

**GET /user/id** - Endpoint responsável por retornar informações de um usuário cadastrado através do id referenciado.
* Exemplo de url: http://localhost:8080/user/1
* Para o endpoint, será necessário passar no header do request a chave "Authorization", com o valor contido no
header de resposta do login para a chave de mesmo nome.
* Exemplo de resposta:

~~~json
{
  "id": 1,
  "name": "Pessoa da Silva",
  "email": "pessoa_da_silva@email.com"
}
~~~

Os testes de unidade estão levando em consideração uma base de testes H2 diferente da base de produção.
No pacote de testes com.bios.user.service o testes dos services.
No pacote de testes com.bios.user.integration teste de integração com a controller do projeto.