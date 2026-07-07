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

# **Secure MCP Servers**

## *With Spring AI*

<br>

### Daniel Garnier-Moiroux

Tanzu Live, 2026-07-07


---
layout: image-right
image: /daniel-intro.jpg
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

## Secure MCP servers with Spring AI

<br>

1. MCP tl;dr
1. Identity and permissions in MCP
1. Spring MCP-compatible auth server
1. Securing the MCP server
1. Securing the MCP client

---

## Secure MCP servers with Spring AI

<br>

1. **MCP tl;dr**
1. Identity and permissions in MCP
1. Spring MCP-compatible auth server
1. Securing the MCP server
1. Securing the MCP client

---

## MCP: Why?

<br>

LLMs create text (or bytes) from their training set.

They lack:

1. Up-to-date information
1. Ability to perform "actions"

Can't perform an "Observe -> Decide -> Act" loop

---

## Enter MCP

<br>

Model Context Protocol

https://modelcontextprotocol.io

By Anthropic, now AAIF

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

## Secure MCP servers with Spring AI

<br>

1. MCP tl;dr
1. **Identity and permissions in MCP**
1. Spring MCP-compatible auth server
1. Securing the MCP server
1. Securing the MCP client

---

## Identity and permissions in MCP

<br>

[MCP Specification](https://modelcontextprotocol.io/specification/2025-11-25/basic/authorization)

Based on OAuth2: the MCP client sends a token to the MCP server.

---

## Challenges

<br>

OAuth2 requires a pre-existing relationship between Client, Resource Server and Auth Server.

That's not what we want to do with MCP: _users_ point their AI app at any server they like.

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

## **⚠️ In the case of MCP**

---
layout: image
image: /oauth-4.png
---

---
layout: image
image: /oauth-5.png
---

---

## Problem: discovering the auth server

<br>

Discovery via MCP server metadata

- **RFC 9728** OAuth 2.0 Protected Resource Metadata
    - MCP Server: `/.well-known/oauth-protected-resource`
- **RFC 8414** OAuth 2.0 Authorization Server Metadata
    - Authz Server: `/.well-known/oauth-authorization-server`

Example: https://mcp.figma.com/mcp

---

## Problem: pre-registration

<br>

No pre-registration needed, either:

- **RFC 7591** OAuth 2.0 Dynamic Client Registration Protocol
    - Authz Server exposes an open `registration_endpoint`
- **Draft** OAuth Client ID Metadata Document
    - Client sends `client_id=https://client.example.com/client`

---
class: smaller
---

## Identity and permissions in MCP

<br>

Evolution across 3 spec versions <v-click at="3">... ⚡️ _it's not quite baked yet_ 💥 ...</v-click>

<v-clicks at="0">

- V1 (2025-03)
    - MCP Server == auth server (issues tokens)
- V2 (2025-06)
    - Actually no, separate auth server
    - "Dynamic Client Registration" (DCR)
- V3 (2025-11):
    - Actually no, no DCR
    - "Client ID Metadata Document"

</v-clicks>


---

## Secure MCP servers with Spring AI

<br>

1. MCP tl;dr
1. Identity and permissions in MCP
1. **Spring MCP-compatible auth server**
1. Securing the MCP server
1. Securing the MCP client

---

## Spring Authz Server + MCP Security

&nbsp;

https://github.com/spring-ai-community/mcp-security

```xml

<dependency>
    <groupId>org.springaicommunity</groupId>
    <artifactId>mcp-authorization-server-spring-boot</artifactId>
    <version>${mcp-security.version}</version>
</dependency>
```

---

## Spring Authz Server + MCP Security

<br>

Supports:
- Pre-registered clients
- DCR
- CIMD

---

## Secure MCP servers with Spring AI

<br>

1. MCP tl;dr
1. Identity and permissions in MCP
1. Spring MCP-compatible auth server
1. **Securing the MCP server**
1. Securing the MCP client

---

## MCP Server Security

&nbsp;

https://github.com/spring-ai-community/mcp-security

```xml

<dependency>
    <groupId>org.springaicommunity</groupId>
    <artifactId>mcp-server-security-spring-boot</artifactId>
    <version>${mcp-security.version}</version>
</dependency>
```

---

## MCP Server Security

<br>

Supports:
- Resource server
- Protected resource metadata

WebMvc only!

---

## Secure MCP servers with Spring AI

<br>

1. MCP tl;dr
1. Identity and permissions in MCP
1. Spring MCP-compatible auth server
1. Securing the MCP server
1. **Securing the MCP client**

---

## MCP Client Security

&nbsp;

https://github.com/spring-ai-community/mcp-security

```xml

<dependency>
    <groupId>org.springaicommunity</groupId>
    <artifactId>mcp-client-security-spring-boot</artifactId>
    <version>${mcp-security.version}</version>
</dependency>
```

---

## MCP Server Security

<br>

Supports:
- DCR
- CIMD

WebMvc + JDK's HttpClient

---

## References

&nbsp;

#### **<logos-github-icon /> https://github.com/Kehrlann/spring-ai-mcp-security**

&nbsp;


- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---
layout: image
hideInToc: true
image: /meet-me.jpg
class: end
---

# **Merci 😊**

