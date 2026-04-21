---
theme: default
class: 'text-center'
highlighter: prism
lineNumbers: true
transition: none
# use UnoCSS
css: unocss
aspectRatio: "16/9"
colorSchema: "light"
canvasWidth: 1024
---

# **Serveurs MCP Sécurisés**

## *Avec Spring AI*

<br>

### Daniel Garnier-Moiroux

Devoxx France, 2026-04-22


---
layout: image-right
image: /daniel-broken-shoulder.jpg
hideInToc: true
class: smaller
---

#### Daniel

### Garnier-Moiroux

<br>

Software Engineer

- <logos-spring-icon /> Spring
- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <logos-github-icon /> github.com/Kehrlann/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---

## Serveurs MCP Sécurisés

<br>

1. Intro à ModelContextProtocol (MCP)
1. MCP avec Spring AI
1. Identité et permissions dans MCP
1. MCP sécurisé avec Spring AI

---

## Serveurs MCP Sécurisés

<br>

1. **Intro à ModelContextProtocol (MCP)**
1. MCP avec Spring AI
1. Identité et permissions dans MCP
1. MCP sécurisé avec Spring AI

---

## Model Context Protocol

<br>

https://modelcontextprotocol.io

Protocol par Anthropic

Maintenant "Agentic AI Foundation" sous Linux Foundation


---

## MCP: Pourquoi?

<br>

Les LLMs font du texte (ou des octets) à partir de leur corpus d'entraînement.

Manquent:

1. Données à jour
1. Capacité d'actions

Impossible faire une boucle "Observe -> Decide -> Act"

---
layout: image
image: /mcp-flow-1.png
---

---
layout: image
image: /mcp-flow-2.png
---

---
layout: image
image: /mcp-flow-3.png
---

---
layout: image
image: /mcp-flow-4.png
---

---
layout: image
image: /mcp-flow-5.png
---

---
layout: image
image: /mcp-flow-6.png
---

---
layout: image
image: /mcp-flow-7.png
---

---
layout: image
image: /mcp-flow-8.png
---

---

## Serveurs MCP Sécurisés

<br>

1. Intro à ModelContextProtocol (MCP)
1. **MCP avec Spring AI**
1. Identité et permissions dans MCP
1. MCP sécurisé avec Spring AI

---

## En pratique avec Spring AI

<br>

Nouvelle appli ou appli existante:

```xml

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

---

## En pratique avec Spring AI

<br>

Programmation par annotations:

- `@McpTool` & `@McpToolParam`
- `@McpResource`
- `@McpPrompt`
- ...

---

## Serveurs MCP Sécurisés

<br>

1. Intro à ModelContextProtocol (MCP)
1. MCP avec Spring AI
1. **Identité et permissions dans MCP**
1. MCP sécurisé avec Spring AI

---

## Identité et permissions dans MCP

<br>

[Spécification MCP](https://modelcontextprotocol.io/specification/2025-11-25/basic/authorization)

Basé sur OAuth2: le client MCP envoie un token au serveur MCP.

---

## Challenges

<br>

OAuth2 demande un lien préexistant entre Client, Resource Server et Auth Server.

Pas ce qu'on veut faire avec MCP: les _utilisateurs_ pointent leur appli AI vers n'importe quel serveur.

---
layout: image
image: /oauth-1.png
---

---
layout: image
image: /oauth-2.png
---

---
layout: image
image: /oauth-3.png
---

---
layout: cover
---

## **⚠️ Dans le cas de MCP**

---
layout: image
image: /oauth-4.png
---

---
layout: image
image: /oauth-5.png
---

---

## Problème: découverte du serveur d'auth

<br>

Découverte via des métadonnées du serveur MCP

- **RFC 9728** OAuth 2.0 Protected Resource Metadata
    - MCP Server: `/.well-known/oauth-protected-resource`
- **RFC 8414** OAuth 2.0 Authorization Server Metadata
    - Authz Server: `/.well-known/oauth-authorization-server`

Exemple: https://mcp.figma.com/mcp

---

## Problème: pré-enregistrement

<br>

Pré-enregistrement pas nécessaire, soit:

- **RFC 7591** OAuth 2.0 Dynamic Client Registration Protocol
    - Authz Server expose `registration_endpoint` ouvert
- **Draft** OAuth Client ID Metadata Document
    - Client envoie `client_id=https://client.example.com/client`

---
class: smaller
---

## Identité et permissions dans MCP

<br>

Évolution sur 3 versions de specs <v-click at="3">... 🤡 _c'est pas tout à fait cuit _ 🤡 ...</v-click>

<v-clicks at="0">

- V1 (2025-03)
    - MCP Server == auth server (crée des tokens)
- V2 (2025-06)
    - Finalement non, auth server à part
    - "Dynamic Client Registration" (DCR)
- V3 (2025-11):
    - Finalement non, pas de DCR
    - "Client ID Metadata Document"

</v-clicks>


---

## Serveurs MCP Sécurisés

<br>

1. Intro à ModelContextProtocol (MCP)
1. MCP avec Spring AI
1. Identité et permissions dans MCP
1. **MCP sécurisé avec Spring AI**

---

## Spring AI

https://github.com/spring-ai-community/mcp-security/

Disponible sur https://start.spring.io

```xml

<dependency>
    <groupId>org.springaicommunity</groupId>
    <artifactId>mcp-server-security-spring-boot</artifactId>
    <version>${mcp-security.version}</version>
</dependency>
```

---

## Spring AI

<br>

Il faut:

1. Configurer son MCP Server
1. Avoir un serveur d'auth compatible
    1. Possible avec Spring Auth Server
1. _Avoir un client compatible_
    1. La plupart des clients!
    1. Possible avec Spring AI

---

## References

&nbsp;

#### **<logos-github-icon /> https://github.com/Kehrlann/spring-ai-mcp-security**

&nbsp;

<!-- qrencode -s 9 -m 2 -o public/rating-devoxxfr.png "https://m.devoxx.com/events/devoxxfr2026/talks/5555/serveurs-mcp-scuriss-avec-spring-ai" -->
<div style="float:right; margin-right: 50px; text-align: center;">
  <a href="https://m.devoxx.com/events/devoxxfr2026/talks/5555/serveurs-mcp-scuriss-avec-spring-ai" target="_blank">
      <img src="/rating-devoxxfr.png" style="margin-top: -20px; max-height: 250px;" >
  </a>
</div>


- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---
layout: two-cols
---

<div style="height: 100%; display: flex; flex: row; justify-content: center; align-items: center;" >
    <img src="/testing-spring-boot-applications-cover.png" style="" >
</div>

::right::

<div style="height: 100%; display: flex; flex-flow: column; justify-content: center; align-items: center;" >
    <img src="/manning-affiliate-qr-code.png" style="display: block;" >
    <div>https://hubs.la/Q04bFz560</div>
    <br>
    <div>45% de remise!</div>
    <br>
    <div><b>devoxxfr26</b></div>
    <br>
    <div>(jusqu'au 9 mai)</div>
</div>


---
layout: image
hideInToc: true
image: /meet-me.jpg
class: end
---

# **Merci 😊**
