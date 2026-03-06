const { createApp } = Vue;

createApp({
  data() {
    return {
      currentSection: "home",
      year: new Date().getFullYear(),
    };
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll, { passive: true });
  },
  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    scrollTo(id) {
      const el = document.getElementById(id);
      if (el) {
        el.scrollIntoView({ behavior: "smooth", block: "start" });
      }
      this.currentSection = id;
    },
    handleScroll() {
      const sections = ["home", "tools", "guide", "about"];
      const scrollPos = window.scrollY || document.documentElement.scrollTop;
      let active = this.currentSection;

      for (const id of sections) {
        const el = document.getElementById(id);
        if (!el) continue;
        const rect = el.getBoundingClientRect();
        const offsetTop = rect.top + scrollPos;
        if (scrollPos + 80 >= offsetTop) {
          active = id;
        }
      }

      this.currentSection = active;
    },
  },
}).mount("#app");

