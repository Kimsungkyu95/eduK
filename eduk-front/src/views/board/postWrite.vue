<template>
  <b-card class="blog-edit-wrapper">
    <!-- form -->
    <b-form class="mt-2">
      <b-row>
        <b-col md="12">
          <b-form-group label="제목" label-for="blog-edit-title" class="mb-2">
            <b-form-input
              id="blog-edit-title"
              placeholder="제목을 입력해주세요"
              v-model="postTitle"
              required
            />
          </b-form-group>
        </b-col>

        <b-col cols="12">
          <b-form-group label="내용" label-for="blog-content" class="mb-2">
            <quill-editor
              id="blog-content"
              :options="snowOption"
              v-model="postContent"
              required
            />
          </b-form-group>
        </b-col>
        <b-col cols="12" class="mb-2">
          <div class="border rounded p-2">
            <h4 class="mb-1">파일 업로드</h4>
            <b-form-file
              v-model="file"
              placeholder="파일을 선택하거나 여기에 올려주세요"
              data-placeholder="여기에 파일 놓기"
              id="uploadFile"
            />

            <b-card-text class="my-1">
              선택한 파일: <strong>{{ file ? file.name : "" }}</strong>
            </b-card-text>
          </div>
        </b-col>
        <b-col cols="12" class="mt-50">
          <b-button
            v-ripple.400="'rgba(255, 255, 255, 0.15)'"
            variant="primary"
            class="mr-1"
            @click="postWrite"
          >
            작성
          </b-button>
          <b-button
            v-ripple.400="'rgba(186, 191, 199, 0.15)'"
            variant="outline-secondary"
            @click="cancel"
          >
            취소
          </b-button>
        </b-col>
      </b-row>
    </b-form>
    <!--/ form -->
  </b-card>
</template>

<script>
import {
  BCard,
  BMedia,
  BAvatar,
  BCardText,
  BMediaAside,
  BMediaBody,
  BForm,
  BRow,
  BCol,
  BFormGroup,
  BFormInput,
  BImg,
  BFormFile,
  BLink,
  BButton,
} from "bootstrap-vue";
import vSelect from "vue-select";
import { quillEditor } from "vue-quill-editor";
import Ripple from "vue-ripple-directive";
import { useInputImageRenderer } from "@core/comp-functions/forms/form-utils";
import { ref } from "@vue/composition-api";
import axios from "axios";

export default {
  components: {
    BCard,
    BMedia,
    BAvatar,
    BCardText,
    BMediaAside,
    BMediaBody,
    BForm,
    BLink,
    BImg,
    BRow,
    BCol,
    BButton,
    BFormGroup,
    BFormInput,
    BFormFile,
    vSelect,
    quillEditor,
  },
  directives: {
    Ripple,
  },
  data() {
    return {
      file: null,
      postTitle: "",
      postContent: "",
      snowOption: {
        theme: "snow",
      },
      boardId: "",
      postId: "",
    };
  },
  mounted() {
    this.boardId = this.$route.params.boardId;
  },
  methods: {
    postWrite() {
      console.log(this.boardId)
      this.$http
        .post("/" + this.boardId + "/post", {
          postTitle: this.postTitle,
          postContent: this.postContent,
        })
        .then((res) => {
          this.postId = res.data.postId;
          if (this.file != null) {
            var frm = new FormData();
            var thisFile = document.getElementById("uploadFile");
            frm.append("file", thisFile.files[0]),
              frm.append("postId", this.postId),
              axios
                .post("https://localhost:1234/file/uploadfile", frm, {
                  headers: {
                    "Content-Type": "multipart/form-data",
                  },
                })
                .then((resp) => {
                  this.postRead();
                })
                .catch((error) => {
                  console.log(error);
                  this.$router.push("/error-404");
                });
          }
          this.postRead();
        })
        .catch((err) => {
          console.log(err + "post 등록 실패");
        });
    },
    postRead() {
      this.$router.push("/" + this.boardId + "/postRead/" + this.postId);
    },
    cancel() {
      this.$router.go(-1);
    },
  },
  setup() {
    const refInputEl = ref(null);
    const refPreviewEl = ref(null);

    const { inputImageRenderer } = useInputImageRenderer(
      refInputEl,
      (base64) => {
        refPreviewEl.value.src = base64;
      }
    );

    return {
      refInputEl,
      refPreviewEl,
      inputImageRenderer,
    };
  },
};
</script>

<style lang="scss">
@import "@core/scss/vue/libs/vue-select.scss";
@import "@core/scss/vue/libs/quill.scss";
@import "@core/scss/vue/pages/page-blog.scss";

.ql-container {
  min-height: 250px;
}
</style>
